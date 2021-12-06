//package CSVValidation
//
//import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
//import com.fasterxml.jackson.databind.JsonNode
//import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeFactory, NullNode, ObjectNode, TextNode}
//import errors.MetadataError
//
//object Column {
//  val datatypeDefaultValue = JsonNodeFactory.instance.objectNode().set("@id", new TextNode("http://www.w3.org/2001/XMLSchema#string"))
//
//  def fromJson(number: Int, columnDesc: ObjectNode, baseUrl: String, lang: String, inheritedProperties: ObjectNode): Column = {
//    val annotations = JsonNodeFactory.instance.objectNode()
//    var warnings = Array[String]()
//    val columnProperties = JsonNodeFactory.instance.objectNode()
//    val inheritedPropertiesCopy = inheritedProperties.deepCopy()
//
//    for((property, value) <- columnDesc.getKeysAndValues) {
//      (property, value) match {
//        case ("@type", v: TextNode) if v.asText != "Column" => {
//          throw new MetadataError(s"columns[$number].@type, @type of column is not 'Column'")
//        }
//        case _ => {
//          val (v, w, csvwPropertyType) = PropertyChecker.checkProperty(property, value, baseUrl, lang)
//          if(w.nonEmpty) {
//            // Add error message objects to warnings array
//          }
//          csvwPropertyType match {
//            case PropertyType.Annotation => annotations.set(property, v)
//            case PropertyType.Common | PropertyType.Column => columnProperties.set(property, v)
//            case PropertyType.Inherited => inheritedPropertiesCopy.set(property, v)
//            case _ => // Add warning to the warnings array using ErrorMessage class
//              //  warnings << Csvlint::ErrorMessage.new(:invalid_property, :metadata, nil, nil, "column: #{property}", nil)
//          }
//        }
//      }
//    }
//    val datatype = if (!inheritedPropertiesCopy.path("datatype").isMissingNode) {
//      inheritedPropertiesCopy.get("datatype")
//    } else {
//      datatypeDefaultValue
//    }
//
//    val newLang = if (!inheritedPropertiesCopy.path("lang").isMissingNode) {
//      inheritedPropertiesCopy.get("lang")
//    } else {
//      new TextNode("und")
//    }
//
//    val nullParam = if(!inheritedPropertiesCopy.path("null").isMissingNode) {
//      inheritedPropertiesCopy.get("null")
//    } else {
//      Array[String]("")
//    }
//
//    val default = if(!inheritedPropertiesCopy.path("default").isMissingNode) {
//      inheritedPropertiesCopy.get("default")
//    } else {
//      new TextNode("")
//    }
//
//    val required = if(!inheritedPropertiesCopy.path("required").isMissingNode) {
//      inheritedPropertiesCopy.get("required")
//    } else {
//      false
//    }
//
//    val defaultName = getDefaultName()
//
//
//
//
//    return Column(number, columnProperties.get("name"), columnProperties.get("@id"),
//                  datatype = datatype, lang = newLang, nullParam = nullParam, default = default,
//                  aboutUrl = inheritedPropertiesCopy.get("aboutUrl"),
//                  propertyUrl = inheritedPropertiesCopy.get("propertyUrl"),
//                  valueUrl = inheritedPropertiesCopy.get("valueUrl"), required = inheritedPropertiesCopy.get("required"),
//                  separator = inheritedPropertiesCopy.get("separator"), ordered = inheritedPropertiesCopy.get("ordered"),
//                  )
//  }
//
//  def getDefaultName(columnProperties: ObjectNode, lang: String):String = {
//    if(!columnProperties.path("titles").path(lang).isMissingNode) {
//      return
//    }
//  }
//}
//
////def initialize(
//// number,
//// name,
//// id: nil,
//// about_url: nil,
//// datatype: { "@id" => "http://www.w3.org/2001/XMLSchema#string" },
//// default: "",
//// lang: "und",
//// null: [""],
//// ordered: false,
//// property_url: nil,
//// required: false,
//// separator: nil,
//// source_number: nil,
//// suppress_output: false,
//// text_direction: :inherit,
//// default_name: nil,
//// titles: {},
//// value_url: nil,
//// virtual: false,
//// annotations: [],
//// warnings: [])
//
//object Column {
//  def fromJson(on: ObjectNode): Column = {
//    // todo: Do all checks and default values in here
//    new Column()
//  }
//}
//
//class Column() {
//  private this(number:Int,
//                  name: String,
//                  id: String,
//                  aboutUrl: String,
//                  datatype: ObjectNode = Column.datatypeDefaultValue,
//                  default: String = "",
//                  lang: String = "und",
//                  nullParam: Array[String] = Array[String](""),
//                  ordered: Boolean = false,
//                  propertyUrl: Option[String] = None,
//                  required: Boolean = false,
//                  separator: Option[String] = None,
//                  suppressOutput: Boolean = false,
//                  textDirection: String = "inherit",
//                  defaultName: String,
//                  titles: Array[String],
//                  valueUrl: Option[String],
//                  virtual: Boolean = false,
//                  annotations: Map[String, JsonNode],
//                  warnings: Array[String]) {
//
//  // remove all default values of Column class
//
//}
