package CSVValidation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{JsonNodeFactory, ObjectNode, TextNode}
import errors.MetadataError

//import scala.jdk.CollectionConverters.IteratorHasAsScala
import traits.JavaIteratorExtensions.IteratorHasAsScalaArray

object Table {

  def fromJson(tableDesc: ObjectNode, baseUrl: String, lang: String, commonProperties: ObjectNode, inheritedProperties: ObjectNode): Table = {
    val annotations = JsonNodeFactory.instance.objectNode()
    val warnings = Array[String]()
    val columns = Array[String]()
    val tableProperties = commonProperties.deepCopy()
    val inheritedPropertiesCopy = inheritedProperties.deepCopy()

    val fieldAndValues = tableDesc.fields().asScalaArray
    for (fieldAndValue <- fieldAndValues) {
      val property = fieldAndValue.getKey
      val value = fieldAndValue.getValue
      (property, value) match {
        case ("@type", s: TextNode) if s.asText != "Table" => {
          throw new MetadataError()
        }

        /**
         * {
         * "@type": "Table"
         * }
         *
         * {
         * "@type": "Chicken burger"
         * }
         *
         * {
         * "@type": {
         * "What???": 111
         * }
         * }
         */
        case ("@type", v) => throw new MetadataError(s"Unexpected value for '@type'. Expected string but got ${v.getNodeType} (${v.toPrettyString})")
        case (_, v) => {
          val (newValue, w, typeString) = PropertyChecker.checkProperty(property, v, baseUrl, lang)
          // Todo: place all warnings received into warnings array using the new ErrorMessage class(to be implemented)
          typeString match {
            case PropertyType.Annotation => annotations.set(property, newValue)
            case PropertyType.Table | PropertyType.Common => tableProperties.set(property, newValue)
            case PropertyType.Column => {
              // Todo: ..
            }
            case _ => inheritedPropertiesCopy.set(property, newValue)
          }
        }
      }
    }
  }
}

    //table_desc.each do |property,value|
    //            v, warning, type = Csvw::PropertyChecker.check_property(property, value, base_url, lang)
    //            warnings += Array(warning).map{ |w| Csvlint::ErrorMessage.new(w, :metadata, nil, nil, "#{property}: #{value}", nil) } unless warning.nil? || warning.empty?
    //            if type == :annotation
    //              annotations[property] = v
    //            elsif type == :table || type == :common
    //              table_properties[property] = v
    //            elsif type == :column
    //              warnings << Csvlint::ErrorMessage.new(:invalid_property, :metadata, nil, nil, "#{property}", nil)
    //            else
    //              inherited_properties[property] = v
    //            end
    //          end
    //        end


  }
}
case class Table() {

}
