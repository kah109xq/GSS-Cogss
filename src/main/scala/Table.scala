package CSVValidation

import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import errors.MetadataError
import traits.JavaIteratorExtensions.IteratorHasAsScalaArray

object Table {

  def fromJson(
      tableDesc: ObjectNode,
      baseUrl: String,
      lang: String,
      commonProperties: ObjectNode,
      inheritedPropertiesIn: ObjectNode
  ): Table = {
    val (annotations, tableProperties, inheritedProperties, warnings) =
      partitionAndValidateTablePropertiesByType(
        commonProperties,
        inheritedPropertiesIn,
        tableDesc,
        baseUrl,
        lang
      )

    val url: String = getUrlEnsureExists(tableProperties)

    val maybeTableSchema: Option[JsonNode] =
      extractTableSchema(tableProperties, inheritedProperties)

    maybeTableSchema match {
      case Some(tableSchema) =>
        createTableForExistingSchema(
          tableSchema,
          inheritedProperties,
          baseUrl,
          lang,
          url,
          tableProperties,
          annotations
        )
      case _ =>
        initializeTableWithDefaults(
          annotations,
          warnings,
          url
        )
    }
  }

  private def partitionAndValidateTablePropertiesByType(
      commonProperties: ObjectNode,
      inheritedProperties: ObjectNode,
      tableDesc: ObjectNode,
      baseUrl: String,
      lang: String
  ): (Map[String, JsonNode], ObjectNode, ObjectNode, Array[ErrorMessage]) = {
    var warnings = Array[ErrorMessage]()
    var annotations = Map[String, JsonNode]()
    val tableProperties = commonProperties.deepCopy()
    val inheritedPropertiesCopy = inheritedProperties.deepCopy()

    for ((property, value) <- tableDesc.getKeysAndValues) {
      (property, value) match {
        case ("@type", s: TextNode) if s.asText != "Table" => {
          throw new MetadataError(
            s"@type of table is not 'Table' - ${tableDesc.get("url").asText()}.@type"
          )
        }
        case ("@type", v) =>
          throw new MetadataError(
            s"Unexpected value for '@type'. Expected string but got ${v.getNodeType} (${v.toPrettyString})"
          )
        case (_, v) => {
          val (newValue, w, typeString) =
            PropertyChecker.checkProperty(property, v, baseUrl, lang)
          warnings = Array
            .concat(
              warnings,
              w.map[ErrorMessage](x =>
                ErrorMessage(x, "metadata", "", "", s"$property : $value", "")
              )
            )
          typeString match {
            case PropertyType.Annotation =>
              annotations += (property -> newValue)
            case PropertyType.Table | PropertyType.Common =>
              tableProperties.set(property, newValue)
            case PropertyType.Column => {
              warnings = warnings :+ ErrorMessage(
                "invalid_property",
                "metadata",
                "",
                "",
                property,
                ""
              )
            }
            case _ => inheritedPropertiesCopy.set(property, newValue)
          }
        }
      }
    }
    (annotations, tableProperties, inheritedPropertiesCopy, warnings)
  }

  private def getUrlEnsureExists(tableProperties: ObjectNode) = {
    val urlNode = tableProperties.path("url")
    if (urlNode.isMissingNode) {
      throw new MetadataError("URL not found for table")
    }
    urlNode.asText()
  }

  private def extractTableSchema(
      tableProperties: ObjectNode,
      inheritedPropertiesCopy: ObjectNode
  ) = {
    if (!tableProperties.path("tableSchema").isMissingNode) {
      Some(tableProperties.get("tableSchema"))
    } else if (!inheritedPropertiesCopy.path("tableSchema").isMissingNode) {
      Some(inheritedPropertiesCopy.get("tableSchema"))
    } else {
      None
    }
  }

  private def createTableForExistingSchema(
      tableSchema: JsonNode,
      inheritedProperties: ObjectNode,
      baseUrl: String,
      lang: String,
      url: String,
      tableProperties: ObjectNode,
      annotations: Map[String, JsonNode]
  ): Table = {

    tableSchema match {
      case tableSchemaObject: ObjectNode => {
        var warnings = Array[ErrorMessage]()

        ensureColumnsNodeIsArray(tableSchemaObject)
          .foreach(w => warnings :+= w)

        setTableSchemaInheritedProperties(
          inheritedProperties,
          tableSchemaObject
        )

        val (columns, columnWarnings) =
          validateAndExtractColumnsFromSchema(
            baseUrl,
            lang,
            inheritedProperties,
            tableSchemaObject
          )
        warnings = warnings.concat(columnWarnings)

        // Collect primary keys in primaryKeyColumns
        val primaryKeyToReturn =
          collectPrimaryKeyColumns(tableSchemaObject, columns)

        // Collect foreign Keys in foreignKeyColumns
        val foreignKeyMappings =
          collectForeignKeyColumns(tableSchemaObject, columns)

        // Collect row titles column
        var rowTitlesColumns =
          collectRowTitlesColumns(tableSchemaObject, columns)

        new Table(
          url = url,
          id = getId(tableProperties),
          columns = columns,
          dialect = getDialect(tableProperties),
          foreignKeys = foreignKeyMappings, // a new type here?
          notes = getNotes(tableProperties),
          primaryKey = primaryKeyToReturn,
          rowTitleColumns = rowTitlesColumns,
          schemaId = getMaybeSchemaIdFromTableSchema(tableSchemaObject),
          suppressOutput = getSuppressOutput(tableProperties),
          annotations = annotations,
          warnings = warnings
        )
      }
      case _ =>
        throw new MetadataError(
          "Table schema not object, not sure how to process"
        )
    }
  }

  private def ensureColumnsNodeIsArray(
      tableSchemaObject: ObjectNode
  ): Option[ErrorMessage] = {
    if (tableSchemaObject.get("columns").isArray) {
      None
    } else {
      tableSchemaObject.set("columns", JsonNodeFactory.instance.arrayNode())
      Some(
        ErrorMessage(
          "invalid_value",
          "metadata",
          "",
          "",
          "columns",
          ""
        )
      )
    }
  }

  private def validateAndExtractColumnsFromSchema(
      baseUrl: String,
      lang: String,
      inheritedProperties: ObjectNode,
      tableSchemaObject: ObjectNode
  ): (Array[Column], Array[ErrorMessage]) = {
    var warnings = Array[ErrorMessage]()

    val columnObjects = tableSchemaObject
      .get("columns")
      .elements()
      .asScalaArray

    val columns = columnObjects.zipWithIndex
      .flatMap(c => {
        val (col, i) = c
        col match {
          case colObj: ObjectNode =>
            Some(
              Column.fromJson(i + 1, colObj, baseUrl, lang, inheritedProperties)
            )
          case _ => {
            warnings = warnings :+ ErrorMessage(
              "invalid_column_description",
              "metadata",
              "",
              "",
              col.toString,
              ""
            )
            None
          }
        }
      })

    val columnNames = columns.flatMap(c => c.name)

    ensureNoDuplicateColumnNames(columnNames)
    ensureVirtualColumnsAfterColumns(columns)
    (columns, warnings)
  }

  private def ensureNoDuplicateColumnNames(columnNames: Array[String]): Unit = {
    val duplicateColumnNames = columnNames
      .groupBy(identity)
      .filter(grp => grp._2.length > 1)
      .keys

    if (duplicateColumnNames.nonEmpty) {
      throw new MetadataError(
        s"Multiple columns named ${duplicateColumnNames.mkString(", ")}"
      )
    }
  }

  private def ensureVirtualColumnsAfterColumns(columns: Array[Column]): Unit = {
    var virtualColumns = false
    for (column <- columns) {
      if (virtualColumns && !column.virtual) {
        throw new MetadataError(
          s"virtual columns before non-virtual column ${column.name} (${column.number})"
        )
      }
      virtualColumns = virtualColumns || column.virtual
    }
  }

  private def collectRowTitlesColumns(
      tableSchemaObject: ObjectNode,
      columns: Array[Column]
  ): Array[Column] = {
    if (!tableSchemaObject.path("rowTitles").isMissingNode) {
      var rowTitlesColumns = Array[Column]()
      val rowTitles = tableSchemaObject.get("rowTitles")
      for (rowTitle <- rowTitles.elements().asScalaArray) {
        val maybeCol = columns.find(col =>
          col.name.isDefined && col.name.get == rowTitle.asText()
        )
        maybeCol match {
          case Some(col) => rowTitlesColumns :+= col
          case None =>
            throw new MetadataError(
              s"rowTitles references non-existant column - ${rowTitle.asText()}"
            )
        }
      }
      rowTitlesColumns
    } else {
      Array[Column]()
    }
  }

  private def collectForeignKeyColumns(
      tableSchemaObject: ObjectNode,
      columns: Array[Column]
  ): Array[ForeignKeyWrapper] = {
    if (tableSchemaObject.path("foreignKeys").isMissingNode) {
      Array()
    } else {
      var foreignKeys = Array[ForeignKeyWrapper]()
      val foreignKeysNode =
        tableSchemaObject.get("foreignKeys").asInstanceOf[ArrayNode]
      for (foreignKey <- foreignKeysNode.elements().asScalaArray) {
        var foreignKeyColumns = Array[Column]()
        for (
          reference <- foreignKey.get("columnReference").elements().asScalaArray
        ) {
          val maybeCol = columns.find(col =>
            col.name.isDefined && col.name.get == reference.asText()
          )
          maybeCol match {
            case Some(col) => foreignKeyColumns :+= col
            case None =>
              throw new MetadataError(
                s"foreignKey references non-existent column - ${reference.asText()}"
              )
          }
        }
        foreignKeys :+= ForeignKeyWrapper(
          foreignKey.asInstanceOf[ObjectNode],
          foreignKeyColumns
        )
      }
      foreignKeys
    }
  }

  private def collectPrimaryKeyColumns(
      tableSchemaObject: ObjectNode,
      columns: Array[Column]
  ): Array[Column] = {
    if (!tableSchemaObject.path("primaryKey").isMissingNode) {
      var w = Array[ErrorMessage]()
      var primaryKeyColumns = Array[Column]()
      val primaryKeys = tableSchemaObject.get("primaryKey")
      var primaryKeyValid = true
      for (reference <- primaryKeys.elements().asScalaArray) {
        val maybeCol = columns.find(col =>
          col.name.isDefined && col.name.get == reference.asText()
        )
        maybeCol match {
          case Some(col) => primaryKeyColumns :+= col
          case None => {
            w = w :+ ErrorMessage(
              "invalid_column_reference",
              "metadata",
              "",
              "",
              s"primaryKey: $reference",
              ""
            )
            primaryKeyValid = false
          }

        }
      }
      if (primaryKeyValid && primaryKeyColumns.nonEmpty)
        return primaryKeyColumns
    }
    Array[Column]()
  }

  private def setTableSchemaInheritedProperties(
      inheritedProperties: ObjectNode,
      tableSchemaObject: ObjectNode
  ): Unit = {
    for ((property, value) <- tableSchemaObject.getKeysAndValues) {
      if (
        Array[String](
          "columns",
          "primaryKey",
          "foreignKeys",
          "rowTitles"
        ).contains(property)
      ) {
        inheritedProperties.set(property, value)
      }
    }
  }

  private def initializeTableWithDefaults(
      annotations: Map[String, JsonNode],
      warnings: Array[ErrorMessage],
      url: String
  ) = {
    new Table(
      url = url,
      id = None,
      columns = Array(),
      dialect = None,
      foreignKeys = Array(),
      notes = None,
      primaryKey = Array(),
      rowTitleColumns = Array(),
      schemaId = None,
      suppressOutput = false,
      annotations = annotations,
      warnings = warnings
    )
  }

  private def getMaybeSchemaIdFromTableSchema(
      schema: ObjectNode
  ): Option[String] = {
    val idNode = schema.path("@id")
    if (idNode.isMissingNode) {
      None
    } else {
      Some(idNode.asText())
    }
  }

  private def getId(tableProperties: ObjectNode): Option[String] = {
    val idNode = tableProperties.path("@id")
    if (idNode.isMissingNode) {
      None
    } else {
      Some(idNode.asText())
    }
  }

  private def getNotes(tableProperties: ObjectNode): Option[ArrayNode] = {
    if (tableProperties.path("notes").isMissingNode) {
      None
    } else {
      tableProperties.get("notes") match {
        case a: ArrayNode => Some(a)
        case _            => throw new MetadataError("Notes property should be an array")
      }
    }
  }

  private def getDialect(tableProperties: ObjectNode): Option[JsonNode] = {
    if (tableProperties.path("dialect").isMissingNode) {
      None
    } else {
      Some(tableProperties.get("dialect"))
    }
  }

  private def getSuppressOutput(tableProperties: ObjectNode): Boolean = {
    if (tableProperties.path("suppressOutput").isMissingNode) {
      false
    } else {
      tableProperties.get("suppressOutput").asBoolean()
    }
  }
}

case class Table private (
    url: String,
    id: Option[String],
    columns: Array[Column],
    dialect: Option[JsonNode],
    foreignKeys: Array[ForeignKeyWrapper],
    notes: Option[ArrayNode],
    primaryKey: Array[Column],
    rowTitleColumns: Array[Column],
    schemaId: Option[String],
    suppressOutput: Boolean,
    annotations: Map[String, JsonNode],
    warnings: Array[ErrorMessage]
) {
  var foreignKeyValues: Map[String, JsonNode] = Map()
  var foreignKeyReferences: Array[ForeignKeyWithTable] = Array()
  var foreignKeyReferenceValues: Map[String, JsonNode] = Map()
}
