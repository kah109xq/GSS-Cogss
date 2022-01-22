package CSVValidation

import CSVValidation.traits.JavaIteratorExtensions.IteratorHasAsScalaArray
import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}

import scala.collection.mutable.Map
object Table {

  def fromJson(
      tableDesc: ObjectNode,
      baseUrl: String,
      lang: String,
      commonProperties: Map[String, JsonNode],
      inheritedPropertiesIn: Map[String, JsonNode]
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
      commonProperties: Map[String, JsonNode],
      inheritedProperties: Map[String, JsonNode],
      tableDesc: ObjectNode,
      baseUrl: String,
      lang: String
  ): (
      Map[String, JsonNode],
      Map[String, JsonNode],
      Map[String, JsonNode],
      Array[ErrorMessage]
  ) = {
    var warnings = Array[ErrorMessage]()
    val annotations = Map[String, JsonNode]()
    val tableProperties: Map[String, JsonNode] =
      MapHelpers.deepCloneJsonPropertiesMap(commonProperties)
    val inheritedPropertiesCopy: Map[String, JsonNode] =
      MapHelpers.deepCloneJsonPropertiesMap(inheritedProperties)
    for ((property, value) <- tableDesc.getKeysAndValues) {
      (property, value) match {
        case ("@type", s: TextNode) if s.asText == "Table" => {}
        case ("@type", s: TextNode) => {
          throw new MetadataError(
            s"@type of table is not 'Table' - ${tableDesc.get("url").asText()}.@type"
          )
        }
        case ("@type", v) =>
          throw new MetadataError(
            s"Unexpected value for '@type'. Expected string but got ${v.getNodeType} (${v.toPrettyString})"
          )
        case _ => {
          val (newValue, w, csvwPropertyType) =
            PropertyChecker.checkProperty(property, value, baseUrl, lang)
          warnings = Array
            .concat(
              warnings,
              w.map[ErrorMessage](x =>
                ErrorMessage(x, "metadata", "", "", s"$property : $value", "")
              )
            )
          csvwPropertyType match {
            case PropertyType.Annotation =>
              annotations += (property -> newValue)
            case PropertyType.Table | PropertyType.Common =>
              tableProperties += (property -> newValue)
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
            case _ => inheritedPropertiesCopy += (property -> newValue)
          }
        }
      }
    }
    (annotations, tableProperties, inheritedPropertiesCopy, warnings)
  }

  private def getUrlEnsureExists(
      tableProperties: Map[String, JsonNode]
  ): String = {
    tableProperties
      .getOrElse("url", throw new MetadataError("URL not found for table"))
      .asText()
  }

  private def extractTableSchema(
      tableProperties: Map[String, JsonNode],
      inheritedPropertiesCopy: Map[String, JsonNode]
  ): Option[JsonNode] = {
    tableProperties
      .get("tableSchema")
      .orElse(inheritedPropertiesCopy.get("tableSchema"))
  }

  private def createTableForExistingSchema(
      tableSchema: JsonNode,
      inheritedProperties: Map[String, JsonNode],
      baseUrl: String,
      lang: String,
      url: String,
      tableProperties: Map[String, JsonNode],
      annotations: Map[String, JsonNode]
  ): Table = {

    tableSchema match {
      case tableSchemaObject: ObjectNode => {
        var warnings = Array[ErrorMessage]()

        ensureColumnsNodeIsArray(tableSchemaObject, url)
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
        val (primaryKeyToReturn, primaryKeyWarnings) =
          collectPrimaryKeyColumns(tableSchemaObject, columns)
        warnings = warnings.concat(primaryKeyWarnings)

        // Collect foreign Keys in foreignKeyColumns
        val foreignKeyMappings =
          collectForeignKeyColumns(tableSchemaObject, columns)

        // Collect row titles column
        val rowTitlesColumns =
          collectRowTitlesColumns(tableSchemaObject, columns)

        new Table(
          url = url,
          id = getId(tableProperties),
          columns = columns,
          dialect = tableProperties.get("dialect"),
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
          s"Table schema must be object for table $url "
        )
    }
  }

  private def ensureColumnsNodeIsArray(
      tableSchemaObject: ObjectNode,
      url: String
  ): Option[ErrorMessage] = {
    val columnsNode = tableSchemaObject.path("columns")
    if (!columnsNode.isMissingNode && columnsNode.isArray) {
      None
    } else {
      tableSchemaObject.set("columns", JsonNodeFactory.instance.arrayNode())
      Some(
        ErrorMessage(
          "invalid_value",
          "metadata",
          "",
          "",
          s"columns is not array for table: $url",
          ""
        )
      )
    }
  }

  private def validateAndExtractColumnsFromSchema(
      baseUrl: String,
      lang: String,
      inheritedProperties: Map[String, JsonNode],
      tableSchemaObject: ObjectNode
  ): (Array[Column], Array[ErrorMessage]) = {
    var warnings = Array[ErrorMessage]()

    val columnObjects = tableSchemaObject
      .get("columns")
      .elements()
      .asScalaArray

    val columns = columnObjects.zipWithIndex
      .flatMap {
        case (col, i) => {
          col match {
            case colObj: ObjectNode =>
              Some(
                Column
                  .fromJson(i + 1, colObj, baseUrl, lang, inheritedProperties)
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
        }
      }

    val columnNames = columns.flatMap(c => c.name)

    ensureNoDuplicateColumnNames(columnNames)
    ensureVirtualColumnsAfterColumns(columns)
    (columns, warnings)
  }

  private def ensureNoDuplicateColumnNames(columnNames: Array[String]): Unit = {
    val duplicateColumnNames = columnNames
      .groupBy(identity)
      .filter { case (_, elements) => elements.length > 1 }
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
          s"virtual columns before non-virtual column ${column.name.get} (${column.columnOrdinal})"
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
  ): (Array[Column], Array[ErrorMessage]) = {
    var warnings = Array[ErrorMessage]()
    if (!tableSchemaObject.path("primaryKey").isMissingNode) {
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
            warnings = warnings :+ ErrorMessage(
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
        return (primaryKeyColumns, warnings)
    }
    (Array[Column](), warnings)
  }

  private def setTableSchemaInheritedProperties(
      inheritedProperties: Map[String, JsonNode],
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
        inheritedProperties += (property -> value)
      }
      () // Ensure return type of for loop is consistent.
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

  private def getId(tableProperties: Map[String, JsonNode]): Option[String] = {
    val idNode = tableProperties.get("@id")
    idNode match {
      case Some(value) => Some(value.asText())
      case _           => None
    }
  }

  private def getNotes(
      tableProperties: Map[String, JsonNode]
  ): Option[ArrayNode] = {
    val notesNode = tableProperties.get("notes")
    notesNode match {
      case Some(value) =>
        value match {
          case a: ArrayNode => Some(a)
          case _            => throw new MetadataError("Notes property should be an array")
        }
      case _ => None
    }
  }

  private def getSuppressOutput(
      tableProperties: Map[String, JsonNode]
  ): Boolean = {
    val suppressOutputNode = tableProperties.get("suppressOutput")
    suppressOutputNode match {
      case Some(value) => value.asBoolean()
      case _           => false
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
    var warnings: Array[ErrorMessage]
) {
  var foreignKeyValues: Map[String, JsonNode] = Map()

  /**
    * This array contains the foreign keys defined in other tables' schemas which reference data inside this table.
    */
  var foreignKeyReferences: Array[ForeignKeyWithTable] = Array()
  var foreignKeyReferenceValues: Map[String, JsonNode] = Map()
  var warningsFromColumns: Array[ErrorMessage] = Array[ErrorMessage]()
  for (c <- columns) {
    for (w <- c.warnings) {
      warningsFromColumns :+= w
    }
  }
  warnings = warnings.concat(warningsFromColumns)
}
