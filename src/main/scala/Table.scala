package CSVValidation

import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeFactory, ObjectNode, TextNode}
import errors.MetadataError
import traits.JavaIteratorExtensions.IteratorHasAsScalaArray

object Table {

  def fromJson(tableDesc: ObjectNode, baseUrl: String, lang: String, commonProperties: ObjectNode, inheritedProperties: ObjectNode): Table = {
    var annotations = Map[String, JsonNode]()
    var warnings = Array[ErrorMessage]()
    var columns = Array[Column]()
    var rowTitlesColumns = Array[Column]()
    var foreignKeyColumns = Array[Column]()
    var foreignKeyMappings = Map[ObjectNode, Array[Column]]()
    var primaryKeyColumns = Array[Column]()
    var primaryKeyToReturn = Array[Column]()
    val tableProperties = commonProperties.deepCopy()
    val inheritedPropertiesCopy = inheritedProperties.deepCopy()

    val fieldAndValues = tableDesc.getKeysAndValues
    for ((property, value) <- fieldAndValues) {
      (property, value) match {
        case ("@type", s: TextNode) if s.asText != "Table" => {
          throw new MetadataError(s"@type of table is not 'Table' - ${tableDesc.get("url").asText()}.@type")
        }
        case ("@type", v) => throw new MetadataError(s"Unexpected value for '@type'. Expected string but got ${v.getNodeType} (${v.toPrettyString})")
        case (_, v) => {
          val (newValue, w, typeString) = PropertyChecker.checkProperty(property, v, baseUrl, lang)
          warnings = Array.concat(warnings, w.map[ErrorMessage](x => ErrorMessage(x, "metadata", "", "", s"$property : $value", "")))
          typeString match {
            case PropertyType.Annotation => annotations += (property -> newValue)
            case PropertyType.Table | PropertyType.Common => tableProperties.set(property, newValue)
            case PropertyType.Column => {
              warnings = warnings :+ ErrorMessage("invalid_property", "metadata", "", "", property, "")
            }
            case _ => inheritedPropertiesCopy.set(property, newValue)
          }
        }
      }
    }
    val urlNode = tableProperties.path("url")
    if(urlNode.isMissingNode) {
      throw new MetadataError("URL not found for table")
    }
    val url = urlNode.asText()

    val maybeTableSchema:Option[JsonNode] = extractTableSchema(tableProperties, inheritedPropertiesCopy)
    var columnNames = Array[String]()
    maybeTableSchema match {
      case Some(tableSchema) => {
        tableSchema match {
          case o:ObjectNode => {
            if (!tableSchema.get("columns").isArray) {
              o.set("columns", JsonNodeFactory.instance.arrayNode())
              warnings = warnings :+ ErrorMessage("invalid_value", "metadata", "", "", "columns", "")
            }
            val fieldAndValues = o.fields().asScalaArray
            for (fieldAndValue <- fieldAndValues) {
              val property = fieldAndValue.getKey
              val value = fieldAndValue.getValue
              if (Array[String]("columns", "primaryKey", "foreignKeys", "rowTitles").contains(property)) {
                inheritedPropertiesCopy.set(property, value)
              }
            }

            var virtualColumns = false
            for((columnDesc,i) <- o.get("columns").elements().asScalaArray.zipWithIndex) {
              columnDesc match {
                case columnDescObject: ObjectNode => {
                  val column = Column.fromJson(i +1, columnDescObject, baseUrl, lang, inheritedPropertiesCopy)
                  if(virtualColumns && !column.isVirtual()) {
                    throw new MetadataError(s"virtual columns before non-virtual column ${column.getName()} ($i)")
                  }
                  virtualColumns = if(virtualColumns) {
                    virtualColumns
                  } else {
                    column.isVirtual()
                  }
                  if(columnNames.contains(column.getName())) {
                    throw new MetadataError(s"Multiple columns named ${column.getName()}")
                  }
                  columnNames = columnNames :+ column.getName()
                  columns = columns :+ column
                }
                case _ => {
                  warnings = warnings :+ ErrorMessage("invalid_column_description", "metadata", "", "", columnDesc.toString, "")
                }
              }
            }

            // Collect primary keys in primaryKeyColumns
            if(!o.path("primaryKey").isMissingNode) {
              val primaryKeys = o.get("primaryKey")
              var primaryKeyValid = true
              for(reference <- primaryKeys.elements().asScalaArray) {
                if (columnNames.contains(reference.asText())) {
                  val i = columnNames.indexOf(reference.asText())
                  primaryKeyColumns = primaryKeyColumns :+ columns(i)
                } else {
                  warnings = warnings :+ ErrorMessage("invalid_column_reference", "metadata", "", "", s"primaryKey: $reference", "")
                  primaryKeyValid = false
                }
              }
              primaryKeyToReturn = if(primaryKeyValid && primaryKeyColumns.nonEmpty) primaryKeyColumns else Array[Column]()
            }

            // Collect foreign Keys in foreignKeyColumns
            if(!o.path("foreignKeys").isMissingNode) {
              val foreignKeys = o.get("foreignKeys").asInstanceOf[ObjectNode]
              for((foreignKey, i) <- foreignKeys.elements().asScalaArray.zipWithIndex) {
                for(reference <- foreignKey.get("columnReference").elements().asScalaArray) {
                  if (columnNames.contains(reference.asText())) {
                    val i = columnNames.indexOf(reference.asText())
                    foreignKeyColumns = foreignKeyColumns :+ columns(i)
                  } else {
                    throw new MetadataError(s"foreignKey references non-existant column - ${reference.asText()}")
                  }
                }
              }
              foreignKeyMappings += (foreignKeys -> foreignKeyColumns)
            }

            // Collect row titles column
            if(!o.path("rowTitles").isMissingNode) {
              val rowTitles = o.get("rowTitles")
              for(rowTitle <- rowTitles.elements().asScalaArray) {
                if (columnNames.contains(rowTitle.asText())) {
                  val i = columnNames.indexOf(rowTitle.asText())
                  rowTitlesColumns = rowTitlesColumns :+ columns(i)
                } else {
                  throw new MetadataError(s"rowTitles references non-existant column - ${rowTitle.asText()}")
                }
              }
            }
            new Table(url = url,
              id = getId(tableProperties),
              columns = columns,
              dialect = getDialect(tableProperties),
              foreignKeys = foreignKeyMappings,
              notes = getNotes(tableProperties),
              primaryKey = primaryKeyToReturn,
              rowTitleColumns = rowTitlesColumns,
              schema = getSchema(maybeTableSchema), // Passing only @id as in the ruby implementation
              suppressOutput = getSuppressOutput(tableProperties),
              annotations = annotations,
              warnings = warnings)
          }
          case _ => throw new MetadataError("Table schema not object, not sure how to process")
        }
      }
      case _ => initializeTableWithDefaults(annotations, warnings, foreignKeyMappings, url)
    }
  }

  private def initializeTableWithDefaults(annotations: Map[String, JsonNode], warnings: Array[ErrorMessage], foreignKeyMappings: Map[ObjectNode, Array[Column]], url: String) = {

    new Table(
      url = url,
      id = None,
      columns = Array(),
      dialect = None,
      foreignKeys = foreignKeyMappings,
      notes = None,
      primaryKey = Array(),
      rowTitleColumns = Array(),
      schema = None,
      suppressOutput = false,
      annotations = annotations,
      warnings = warnings
    )

  }

  private def extractTableSchema(tableProperties: ObjectNode, inheritedPropertiesCopy: ObjectNode) = {
    if (!tableProperties.path("tableSchema").isMissingNode) {
      Some(tableProperties.get("tableSchema"))
    } else if (!inheritedPropertiesCopy.path("tableSchema").isMissingNode) {
      Some(inheritedPropertiesCopy.get("tableSchema"))
    } else {
      None
    }
  }

  def getSchema(maybeTableSchema: Option[JsonNode]): Option[String] = {
    maybeTableSchema match {
      case Some(schema) if(!schema.path("@id").isMissingNode) => Some(schema.get("@id").asText())
      case _ => None
    }
  }

  def getId(tableProperties: ObjectNode):Option[String] = {
    val idNode = tableProperties.path("@id")
    if(idNode.isMissingNode) {
      None
    } else {
      Some(idNode.asText())
    }
  }
  def getNotes(tableProperties:ObjectNode):Option[ArrayNode] = {
    if(tableProperties.path("notes").isMissingNode) {
      None
    } else {
      tableProperties.get("notes") match {
        case a:ArrayNode => Some(a)
        case _ => throw new MetadataError("Notes property should be an array")
      }
    }
  }

  def getDialect(tableProperties:ObjectNode):Option[JsonNode] = {
    if(tableProperties.path("dialect").isMissingNode) {
      None
    } else {
      Some(tableProperties.get("dialect"))
    }
  }

  def getSuppressOutput(tableProperties:ObjectNode):Boolean = {
    if(tableProperties.path("suppressOutput").isMissingNode) {
      false
    } else {
      tableProperties.get("suppressOutput").asBoolean()
    }
  }
}
class Table private(url: String,
                    id: Option[String],
                    columns: Array[Column],
                    dialect: Option[JsonNode],
                    foreignKeys: Map[ObjectNode, Array[Column]],
                    notes: Option[ArrayNode],
                    primaryKey: Array[Column],
                    rowTitleColumns: Array[Column],
                    schema: Option[String],
                    suppressOutput: Boolean,
                    annotations: Map[String, JsonNode],
                    warnings: Array[ErrorMessage]) {

}
