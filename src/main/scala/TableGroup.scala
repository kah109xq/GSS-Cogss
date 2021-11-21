package CSVValidation

import Errors.MetadataError
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeFactory, ObjectNode, TextNode}

import java.net.{URI, URL}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object TableGroup {
  val csvwContextUri = "http://www.w3.org/ns/csvw"
  val validProperties: Array[String] = Array[String]("tables", "notes", "@type")

  def fromJson(uri: String, json: ObjectNode): TableGroup = {
    val warnings = Array[String]()
    val tables = JsonNodeFactory.instance.objectNode()
    val annotations = JsonNodeFactory.instance.objectNode()
    val inheritedProperties = JsonNodeFactory.instance.objectNode()
    val commonProperties = JsonNodeFactory.instance.objectNode()
    var baseUrl = uri.trim
    val containsWhitespaces = ".*\s.*".r
    val matcher = containsWhitespaces.pattern.matcher(baseUrl)
    if (matcher.matches()) {
      println("Warning: The path/url has whitespaces in it, please ensure its correctness. Proceeding with received path/url ..")
    }
    var lang = "und"

    val context = json.get("@context")
    context match {
      case a: ArrayNode => {
        a.size() match {
          case 2 => {
            val contextOne = a.get(1)
            contextOne match {
              case o: ObjectNode => {
                val propertyAndValues = Array.from(o.fields().asScala)
                for (propertyAndValue <- propertyAndValues) {
                  val property = propertyAndValue.getKey
                  val value = propertyAndValue.getValue
                  val (newValue, warning, typeString) = PropertyChecker.checkProperty(property, value, baseUrl, lang)
                  if (warning.isEmpty) {
                    if (typeString == PropertyType.Context) {
                      property match {
                        case "@base" => baseUrl = newValue.asText()
                        case "@language" => lang = newValue.asText()
                      }
                    } else {
                      throw new MetadataError(s"@context contains properties other than @base or @language $property)")
                    }
                  } else {
                    if (!Array[String]("@base", "@language").contains(property)) {
                      throw new MetadataError(s"@context contains properties other than @base or @language $property)")
                      // Error Message class to deal with each of the warnings received
                    }
                  }
                }
              }
              // https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#top-level-properties
              case _ => throw new MetadataError("Second @context array value must be an object")
            }
          }
          case 1 => {
            a.get(0) match {
              case s: TextNode if s.asText == csvwContextUri => {}
              case _ => throw new MetadataError(s"First item in @context must be string \"$csvwContextUri\"")
            }
          }
          case l => throw new MetadataError(s"Unexpected @context array length $l")
        }
      }
      case s: TextNode if s.asText == "http://www.w3.org/ns/csvw" => {}
      case _ => throw new MetadataError("Invalid Context")
    }

    json.remove("@context")

    val newJson = JsonNodeFactory.instance.objectNode()
    if (json.path("tables").isMissingNode) {
      if (!json.path("url").isMissingNode) {
        val jsonArrayNode = JsonNodeFactory.instance.arrayNode()
        jsonArrayNode.insert(0, json)
        newJson.set("tables", jsonArrayNode)
      }


      doTheOtherStuff(annotations, inheritedProperties, commonProperties, baseUrl, lang, newJson)


    }
  }

  private def doTheOtherStuff(annotations: ObjectNode, inheritedProperties: ObjectNode, commonProperties: ObjectNode, baseUrl: String, lang: String, json: ObjectNode) = {
    val propertyAndValues = Array.from(json.fields().asScala)
    for (propertyAndValue <- propertyAndValues) {
      val property = propertyAndValue.getKey
      val value = propertyAndValue.getValue
      if (!validProperties.contains(property)) {
        val (newValue, warning, typeString) = PropertyChecker.checkProperty(property, value, baseUrl, lang)
        // deal with warnings received in new error message class
        typeString match {
          case PropertyType.Annotation => annotations.set(property, newValue)
          case PropertyType.Common => commonProperties.set(property, newValue)
          case PropertyType.Inherited => inheritedProperties.set(property, newValue)
          case _ => {} // deal with invalid property error message using the error message class
        }
      }
    }

    val id = Option(commonProperties.get("@id"))
    if (!json.path("@type").isMissingNode && json.get("@type").asText != "TableGroup") {
      throw new MetadataError("@type of table group is not 'TableGroup'")
    }

    json.path("tables") match {
      case t: ArrayNode if t.isEmpty() => throw new MetadataError("Empty tables property")
      case t: ArrayNode => {
        val tableElements = Array.from(t.elements().asScala)
        for(tableElement <- tableElements) {
          tableElement match {
            case o:ObjectNode => {
              var tableUrl = tableElement.get("url")
              if (!tableUrl.isTextual) {
                // Add to warnings
                tableUrl = new TextNode("")
              }
              val uri = URI.create(baseUrl)
              tableUrl = new TextNode(uri.resolve(uri.getPath + tableUrl).toString)
              tableElement.asInstanceOf[ObjectNode].set("url", tableUrl)
              // Create table element and place in tables array / decide the type of tables array
              // Table from json not implemented
            }
            case _ => {} // create error message using error message class and place in warnings
          }
        }
      }
      case n if n.isMissingNode => throw new MetadataError("No tables property")
      case _ => throw new MetadataError("Tables property is not an array")
    }

    // To do the last part ie iterating through tables array and doing stuff can be done only after completing
    // tables class with from json implementation
  }

  case class TableGroup() {

  }

}