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
import errors.MetadataError
import scala.collection.mutable.Map

import java.net.{URI, URL}

object TableGroup {
  val csvwContextUri = "http://www.w3.org/ns/csvw"
  val validProperties: Array[String] = Array[String]("tables", "notes", "@type")

  def fromJson(json: ObjectNode, baseUri: String): TableGroup = {
    var baseUrl = baseUri.trim
    val containsWhitespaces = ".*\\s.*".r
    val matcher = containsWhitespaces.pattern.matcher(baseUrl)
    if (matcher.matches()) {
      println(
        "Warning: The path/url has whitespaces in it, please ensure its correctness. Proceeding with received " +
          "path/url .."
      )
    }
    val processContextResult = processContext(json, baseUrl, "und")
    baseUrl = processContextResult._1
    val lang = processContextResult._2

    if (json.path("tables").isMissingNode) {
      if (!json.path("url").isMissingNode) {
        val jsonArrayNode = JsonNodeFactory.instance.arrayNode()
        jsonArrayNode.insert(0, json)
        json.set("tables", jsonArrayNode)
      }
    } // Can be a function ???

    doTheOtherStuff(
      baseUrl,
      lang,
      json
    )
  }

  def processContext(
      json: ObjectNode,
      baseUrl: String,
      lang: String
  ): (String, String) = {
    var baseUrlNew = baseUrl
    var langNew: String = lang
    val context = json.get("@context")
    context match {
      case a: ArrayNode => {
        a.size() match {
          case 2 => {
            val contextOne = a.get(1)
            contextOne match {
              case o: ObjectNode => {
                for ((property, value) <- o.getKeysAndValues) {
                  val (newValue, warning, typeString) = PropertyChecker
                    .checkProperty(property, value, baseUrl, lang)
                  if (warning.isEmpty) {
                    if (typeString == PropertyType.Context) {
                      property match {
                        case "@base"     => baseUrlNew = newValue.asText()
                        case "@language" => langNew = newValue.asText()
                      }
                    } else {
                      throw new MetadataError(
                        s"@context contains properties other than @base or @language $property)"
                      )
                    }
                  } else {
                    if (
                      !Array[String]("@base", "@language").contains(property)
                    ) {
                      throw new MetadataError(
                        s"@context contains properties other than @base or @language $property)"
                      )
                      // Error Message class to deal with each of the warnings received
                    }
                  }
                }
              }
              // https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#top-level-properties
              case _ =>
                throw new MetadataError(
                  "Second @context array value must be an object"
                )
            }
          }
          case 1 => {
            a.get(0) match {
              case s: TextNode if s.asText == csvwContextUri => {}
              case _ =>
                throw new MetadataError(
                  s"First item in @context must be string ${csvwContextUri} "
                )
            }
          }
          case l =>
            throw new MetadataError(s"Unexpected @context array length $l")
        }
      }
      case s: TextNode if s.asText == "http://www.w3.org/ns/csvw" => {}
      case _                                                      => throw new MetadataError("Invalid Context")
    }
    json.remove("@context")
    (baseUrlNew, langNew)
  }

  private def doTheOtherStuff(
      baseUrl: String,
      lang: String,
      json: ObjectNode
  ): TableGroup = {

    var (annotations, commonProperties, inheritedProperties, warnings) =
      classifyPropertiesBasedOnPropertyTypeAndSetWarnings(
        json,
        baseUrl,
        lang
      )

    val id = getId(commonProperties)
    ensureTypeofTableGroup(json)

    var (tables, w) = createTableObjectsAndSetWarnings(
      json,
      baseUrl,
      lang,
      commonProperties,
      inheritedProperties
    )
    warnings = warnings.concat(w)

    for ((tableUrl, table) <- tables) {
      for ((foreignKey, i) <- table.foreignKeys.zipWithIndex) {
        val reference = foreignKey.jsonObject.get("reference")
        val resourceNode = reference.path("resource")
        val referencedTable: Table = setReferencedTableOrThrowException(
          baseUrl,
          tables,
          tableUrl,
          i,
          reference,
          resourceNode
        )
        var tableColumns: Map[String, Column] = Map()
        for (column <- referencedTable.columns) {
          column.name match {
            case Some(columnName) => tableColumns += (columnName -> column)
            case None             => {}
          }
        }
        var referencedColumns: Array[Column] = reference
          .get("columnReference")
          .elements()
          .asScalaArray
          .map(columnReference => {
            tableColumns.get(columnReference.asText()) match {
              case Some(column) => column
              case None =>
                throw new MetadataError(
                  s"column named ${columnReference.asText()} does not exist in #{resource}," +
                    s" $$.tables[?(@.url = '${tableUrl}')].tableSchema.foreign_keys[${i}].reference.columnReference"
                )
            }
          })

        val foreignKeyWithTable =
          ForeignKeyWithTable(foreignKey, referencedTable, referencedColumns)
        referencedTable.foreignKeyReferences :+= foreignKeyWithTable
      }
    }
    return TableGroup(
      baseUrl,
      id,
      tables,
      getNotes(commonProperties),
      annotations,
      warnings
    )
  }

  private def setReferencedTableOrThrowException(
      baseUrl: String,
      tables: Map[String, Table],
      tableUrl: String,
      i: Int,
      reference: JsonNode,
      resourceNode: JsonNode
  ): Table = {
    if (resourceNode.isMissingNode) {
      val schemaReferenceNode = reference.get("schemaReference")
      val schemaUrl = {
        new URL(new URL(baseUrl), schemaReferenceNode.asText()).toString
      }
      val referencedTables = List.from(
        tables.values.filter(t =>
          t.schemaId.isDefined && t.schemaId.get == schemaUrl
        )
      )
      referencedTables match {
        case referencedTable :: _ => referencedTable
        case Nil =>
          throw new MetadataError(
            s"Could not find foreign key referenced schema ${schemaUrl}, " +
              s"$$.tables[?(@.url = '${tableUrl}')].tableSchema.foreignKeys[${i}].reference.SchemaReference"
          )
      }
    } else {
      val resource = new URL(
        new URL(baseUrl),
        reference.get("resource").asText()
      ).toString
      tables.get(resource) match {
        case Some(refTable) => refTable
        case None =>
          throw new MetadataError(
            s"Could not find foreign key referenced table ${tableUrl}, " +
              s"$$.tables[?(@.url = '${tableUrl}')].tableSchema.foreignKeys[${i}].reference.resource"
          )
      }
    }
  }

  private def ensureTypeofTableGroup(json: ObjectNode) = {
    if (
      !json
        .path("@type")
        .isMissingNode && json.get("@type").asText != "TableGroup"
    ) {
      throw new MetadataError("@type of table group is not 'TableGroup'")
    }
  }

  def classifyPropertiesBasedOnPropertyTypeAndSetWarnings(
      json: ObjectNode,
      baseUrl: String,
      lang: String
  ): (
      Map[String, JsonNode],
      Map[String, JsonNode],
      Map[String, JsonNode],
      Array[ErrorMessage]
  ) = {
    var annotations = Map[String, JsonNode]()
    var commonProperties = Map[String, JsonNode]()
    var inheritedProperties = Map[String, JsonNode]()
    var warnings = Array[ErrorMessage]()
    for ((property, value) <- json.getKeysAndValues) {
      if (!validProperties.contains(property)) {
        val (newValue, w, typeString) =
          PropertyChecker.checkProperty(property, value, baseUrl, lang)
        warnings = w.map[ErrorMessage](x =>
          ErrorMessage(x, "metadata", "", "", s"$property : $value", "")
        )
        typeString match {
          case PropertyType.Annotation => annotations += (property -> newValue)
          case PropertyType.Common     => commonProperties += (property -> newValue)
          case PropertyType.Inherited =>
            inheritedProperties += (property -> newValue)
          case _ => {
            warnings :+= ErrorMessage(
              "invalid_property",
              "metadata",
              "",
              "",
              property,
              ""
            )
          }
        }
      }
      ()
    }
    (annotations, commonProperties, inheritedProperties, warnings)
  }

  private def createTableObjectsAndSetWarnings(
      json: ObjectNode,
      baseUrl: String,
      lang: String,
      commonProperties: Map[String, JsonNode],
      inheritedProperties: Map[String, JsonNode]
  ): (Map[String, Table], Array[ErrorMessage]) = {
    var warnings = Array[ErrorMessage]()
    var tables = Map[String, Table]()
    json.path("tables") match {
      case t: ArrayNode if t.isEmpty() =>
        throw new MetadataError("Empty tables property")
      case t: ArrayNode => {
        for (tableElement <- t.elements().asScalaArray) {
          tableElement match {
            case tableElementObject: ObjectNode => {
              var tableUrl = tableElement.get("url")
              if (!tableUrl.isTextual) {
                warnings = warnings :+ ErrorMessage(
                  "invalid_url",
                  "metadata",
                  "",
                  "",
                  s"url: $tableUrl",
                  ""
                )
                tableUrl = new TextNode("")
              }
              tableUrl = new TextNode(
                new URL(new URL(baseUrl), tableUrl.asText()).toString
              )
              tableElementObject.set("url", tableUrl)
              val table = Table.fromJson(
                tableElementObject,
                baseUrl,
                lang,
                commonProperties,
                inheritedProperties
              )
              tables += (tableUrl.asText -> table)
            }
            case _ => {
              warnings = warnings :+ ErrorMessage(
                "invalid_table_description",
                "metadata",
                "",
                "",
                tableElement.toPrettyString,
                ""
              )
            }
          }
        }
      }
      case n if n.isMissingNode => throw new MetadataError("No tables property")
      case _                    => throw new MetadataError("Tables property is not an array")
    }
    (tables, warnings)
  }

  private def getId(commonProperties: Map[String, JsonNode]) = {
    commonProperties
      .get("@id")
      .map(idNode => idNode.asText())
  }

  def getNotes(commonProperties: Map[String, JsonNode]): Option[JsonNode] = {
    commonProperties.get("notes")
  }

  case class TableGroup private (
      baseUrl: String,
      id: Option[String],
      tables: Map[String, Table],
      notes: Option[JsonNode],
      annotations: Map[String, JsonNode],
      warnings: Array[ErrorMessage]
  ) {}

}
