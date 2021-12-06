//package CSVValidation
//
//import com.fasterxml.jackson.databind.JsonNode
//import com.fasterxml.jackson.databind.node.{JsonNodeFactory, ObjectNode, TextNode}
//import errors.MetadataError
//
////import scala.jdk.CollectionConverters.IteratorHasAsScala
//import traits.JavaIteratorExtensions.IteratorHasAsScalaArray
//
//object Table {
//
//  def fromJson(tableDesc: ObjectNode, baseUrl: String, lang: String, commonProperties: ObjectNode, inheritedProperties: ObjectNode): Table = {
//    val annotations = JsonNodeFactory.instance.objectNode()
//    val warnings = Array[String]()
//    val columns = Array[String]()
//    val tableProperties = commonProperties.deepCopy()
//    val inheritedPropertiesCopy = inheritedProperties.deepCopy()
//
//    val fieldAndValues = tableDesc.fields().asScalaArray
//    for (fieldAndValue <- fieldAndValues) {
//      val property = fieldAndValue.getKey
//      val value = fieldAndValue.getValue
//      (property, value) match {
//        case ("@type", s: TextNode) if s.asText != "Table" => {
//          throw new MetadataError() // Provide more info in this error
//        }
//
//        /**
//         * {
//         * "@type": "Table"
//         * }
//         *
//         * {
//         * "@type": "Chicken burger"
//         * }
//         *
//         * {
//         * "@type": {
//         * "What???": 111
//         * }
//         * }
//         */
//        case ("@type", v) => throw new MetadataError(s"Unexpected value for '@type'. Expected string but got ${v.getNodeType} (${v.toPrettyString})")
//        case (_, v) => {
//          val (newValue, w, typeString) = PropertyChecker.checkProperty(property, v, baseUrl, lang)
//          // Todo: place all warnings received into warnings array using the new ErrorMessage class(to be implemented)
//          typeString match {
//            case PropertyType.Annotation => annotations.set(property, newValue)
//            case PropertyType.Table | PropertyType.Common => tableProperties.set(property, newValue)
//            case PropertyType.Column => {
//              // Todo: ..
//            }
//            case _ => inheritedPropertiesCopy.set(property, newValue)
//          }
//        }
//      }
//    }
//
//  }
//}
//case class Table() {
//
//}
