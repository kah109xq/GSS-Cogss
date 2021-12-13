package CSVValidation

import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeFactory, NullNode, ObjectNode, TextNode}
import errors.MetadataError
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Column {
  val datatypeDefaultValue = JsonNodeFactory.instance.objectNode().set("@id", new TextNode("http://www.w3.org/2001/XMLSchema#string"))

  def getOrdered(inheritedProperties: ObjectNode):Boolean = {
    if(!inheritedProperties.path("ordered").isMissingNode) {
      inheritedProperties.path("ordered").asBoolean
    } else {
      false
    }
  }

  def getTextDirection(inheritedProperties: ObjectNode):String = {
    if(!inheritedProperties.path("textDirection").isMissingNode) {
      inheritedProperties.path("textDirection").asText()
    } else {
      "inherit"
    }
  }

  def getSuppressOutput(columnProperties:ObjectNode):Boolean = {
    if(!columnProperties.path("suppressOutput").isMissingNode) {
      columnProperties.path("suppressOutput").asBoolean
    } else {
      false
    }
  }

  def getVirtual(columnProperties:ObjectNode):Boolean = {
    if(!columnProperties.path("virtual").isMissingNode) {
      columnProperties.path("virtual").asBoolean
    } else {
      false
    }
  }

  def getRequired(inheritedProperties: ObjectNode):Boolean = {
    if(!inheritedProperties.path("required").isMissingNode) {
      inheritedProperties.path("required").asBoolean()
    } else {
      false
    }
  }

  def getDefault(inheritedProperties:ObjectNode):String = {
    if(!inheritedProperties.path("default").isMissingNode) {
      inheritedProperties.get("default").asText()
    } else {
      ""
    }
  }

  def getName(columnProperties:ObjectNode, lang: String):String = {
    if(!columnProperties.path("name").isMissingNode) {
      columnProperties.get("name").asText()
    } else if((!columnProperties.path("titles").isMissingNode) && (!columnProperties.path("titles").path(lang).isMissingNode)) {
      val langArray = Array.from(columnProperties.path("titles").path(lang).elements().asScala)
      langArray(0).asText()
    } else {
      // Not sure what to return here. Hope it does not reach here
      ""
    }
  }

  def getNullParam(inheritedProperties: ObjectNode):Array[String] = {
    if(!inheritedProperties.path("null").isMissingNode) {
      inheritedProperties.get("null") match {
        case a:ArrayNode => {
          var nullParamsToReturn = Array[String]()
          val nullParams = Array.from(a.elements.asScala)
          for(np <- nullParams) nullParamsToReturn = nullParamsToReturn :+ np.asText()
          nullParamsToReturn
        }
        case s:TextNode => Array[String](s.asText())
      }
    } else {
      Array[String]("")
    }
  }

  def fromJson(number: Int, columnDesc: ObjectNode, baseUrl: String, lang: String, inheritedProperties: ObjectNode): Column = {
    var annotations = Map[String, JsonNode]()
    var warnings = Array[String]()
    val columnProperties = JsonNodeFactory.instance.objectNode()
    val inheritedPropertiesCopy = inheritedProperties.deepCopy()

    for((property, value) <- columnDesc.getKeysAndValues) {
      (property, value) match {
        case ("@type", v: TextNode) if v.asText != "Column" => {
          throw new MetadataError(s"columns[$number].@type, @type of column is not 'Column'")
        }
        case _ => {
          val (v, w, csvwPropertyType) = PropertyChecker.checkProperty(property, value, baseUrl, lang)
          if(w.nonEmpty) {
            // Add error message objects to warnings array
          }
          csvwPropertyType match {
            case PropertyType.Annotation => annotations += (property -> v)
            case PropertyType.Common | PropertyType.Column => columnProperties.set(property, v)
            case PropertyType.Inherited => inheritedPropertiesCopy.set(property, v)
            case _ => // Add warning to the warnings array using ErrorMessage class
            //  warnings << Csvlint::ErrorMessage.new(:invalid_property, :metadata, nil, nil, "column: #{property}", nil)
          }
        }
      }
    }
    val datatype = if (!inheritedPropertiesCopy.path("datatype").isMissingNode) {
      inheritedPropertiesCopy.get("datatype")
    } else {
      datatypeDefaultValue
    }

    val newLang = if (!inheritedPropertiesCopy.path("lang").isMissingNode) {
      inheritedPropertiesCopy.get("lang").asText()
    } else {
      "und"
    }

    new Column(number = number, name = getName(columnProperties, lang), // add more logic,
      id = columnProperties.get("@id"), // Null
      datatype = datatype,
      lang = newLang,
      nullParam = getNullParam(inheritedPropertiesCopy),
      default = getDefault(inheritedPropertiesCopy),
      required = getRequired(inheritedPropertiesCopy),
      aboutUrl = inheritedPropertiesCopy.get("aboutUrl").asText(), // What if not present?
      propertyUrl = Some(inheritedPropertiesCopy.get("propertyUrl").asText()), // What if not present?
      valueUrl = Some(inheritedPropertiesCopy.get("valueUrl").asText()), // What if not present?
      separator = Some(inheritedPropertiesCopy.get("separator").asText()),
      ordered = getOrdered(inheritedPropertiesCopy),
      titles = columnProperties.get("titles"), // Keeping it as jsonNode, revisit
      suppressOutput = getSuppressOutput(columnProperties),
      virtual = getVirtual(columnProperties),
      textDirection = getTextDirection(inheritedPropertiesCopy),
      annotations = annotations,
      warnings = warnings)
  }
}

class Column private (number:Int,
                      name: String,
                      id: JsonNode,
                      aboutUrl: String,
                      datatype: JsonNode,
                      default: String,
                      lang: String,
                      nullParam: Array[String],
                      ordered: Boolean,
                      propertyUrl: Option[String],
                      required: Boolean,
                      separator: Option[String],
                      suppressOutput: Boolean,
                      textDirection: String,
//                      defaultName: String, // Not used, this logic is included in name param
                      titles: JsonNode,
                      valueUrl: Option[String],
                      virtual: Boolean,
                      annotations: Map[String, JsonNode],
                      warnings: Array[String]) {
}
