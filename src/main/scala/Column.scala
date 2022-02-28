package CSVValidation

import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}

import java.math.BigInteger
import java.util.OptionalLong
import scala.collection.mutable.Map
import scala.jdk.CollectionConverters.IteratorHasAsScala

object Column {
  val datatypeDefaultValue: ObjectNode = JsonNodeFactory.instance
    .objectNode()
    .set("@id", new TextNode("http://www.w3.org/2001/XMLSchema#string"))

  val DatatypeParser
      : Map[String, (String, Option[String]) => Either[String, Any]] = Map(
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral" -> trimValue(),
    "http://www.w3.org/1999/02/22-rdf-syntax-ns#HTML" -> trimValue(),
    "http://www.w3.org/ns/csvw#JSON" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#anyAtomicType" -> allValueValid(),
    "http://www.w3.org/2001/XMLSchema#anyURI" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#base64Binary" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#hexBinary" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#QName" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#string" -> allValueValid(),
    "http://www.w3.org/2001/XMLSchema#normalizedString" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#token" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#language" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#Name" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#NMTOKEN" -> trimValue(),
    "http://www.w3.org/2001/XMLSchema#boolean" -> processBooleanDatatype(),
    "http://www.w3.org/2001/XMLSchema#decimal" -> processDecimalDatatype(),
    "http://www.w3.org/2001/XMLSchema#integer" -> processIntegerDatatype(),
    "http://www.w3.org/2001/XMLSchema#long" -> processLongDatatype(),
    "http://www.w3.org/2001/XMLSchema#int" -> processIntDatatype(),
    "http://www.w3.org/2001/XMLSchema#short" -> processShortDatatype()
  )

  def trimValue(): (String, Option[String]) => Either[String, String] =
    (v, _) => Right(v.strip())

  def allValueValid(): (String, Option[String]) => Either[String, String] =
    (v, _) => Right(v)

  // In csvlint the native boolean types are returned from this function. Returning strings to make it in line with
  // other datatype validation functions
  def processBooleanDatatype()
      : (String, Option[String]) => Either[String, Boolean] = { (v, format) =>
    {
      Left("")
      // tdo: Cope with where format hasn't been specified.
      // retun boolean
      // split on   | and do things
//        else {
//        val arrayNodeObject = JsonNodeFactory.instance.arrayNode()
//        arrayNodeObject.add(formatValues(0))
//        arrayNodeObject.add(formatValues(1))
//        objectNode.replace("format", arrayNodeObject)
//      }
//      var tupleToReturn = (v, "invalid_boolean")
//      if (format.isArray) {
//        if (v == format.get(0).asText()) {
//          tupleToReturn = ("true", "")
//        } else if (v == format.get(1).asText()) {
//          tupleToReturn = ("false", "")
//        }
//      } else if (format.isMissingNode) {
//        if (Array[String]("true", "1").contains(v)) {
//          tupleToReturn = ("true", "")
//        } else if (Array[String]("false", "0").contains(v)) {
//          tupleToReturn = ("false", "")
//        } `format: "Y|N"`
//      }
//      tupleToReturn
    }
  }

  def processDecimalDatatype()
      : (String, Option[String]) => Either[String, Double] = {
    (value, format) =>
      {
        Left("hi")
//        val validDecimalDatatypeRegex =
//          "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)".r
//        if (!validDecimalDatatypeRegex.pattern.matcher(value).matches()) {
//          Left("invalid_decimal")
//        } else {
//          val f = numericParser(value, format)
//          Right(0.3d)
//        }
      }
  }

  def processIntegerDatatype()
      : (String, Option[String]) => Either[String, BigInteger] = {
    (value, format) =>
      {
        numericParser(value, format) match {
          case Right(newValue) => {
            try {
              val bigIntValue = newValue match {
                case _: java.lang.Long | _: Integer | _: java.lang.Short =>
                  BigInteger.valueOf(newValue.longValue())
                case bigDecimalValue: com.ibm.icu.math.BigDecimal =>
                  bigDecimalValue.toBigIntegerExact
                case _ =>
                  throw new IllegalArgumentException(
                    s"Unexpected type ${newValue.getClass}"
                  )
              }
              Right(bigIntValue)
            } catch {
              case e => Left(s"invalid_integer - '$value' - ${e.getMessage}")
            }
          }
          case Left(warning) => Left(s"invalid_integer - '${value}' ${warning}")
        }
      }
  }

  def processLongDatatype()
      : (String, Option[String]) => Either[String, Long] = { (value, format) =>
    {
      Left("haii")
//        val validLongDatatypeRegex = "[\\-+]?[0-9]+".r
//        if (!validLongDatatypeRegex.pattern.matcher(value).matches()) {
//          Left("invalid_integer")
//        } else {
//          numericParser(value, format) match {
//            case Left(warning) => Left(s"invalid_long - ${warning}")
//            case Right(newValue) => {
//              val doubleValue = newValue.doubleValue()
//              if (doubleValue > Long.MaxValue || doubleValue < Long.MinValue) {
//                Left(s"invalid_long - '$value' Outside Long Range")
//              } else Right(newValue.longValue())
//            }
//          }
//        }
    }
  }

  def processIntDatatype(): (String, Option[String]) => Either[String, Int] = {
    (value, format) =>
      {
        Left("hi")
//        val f = processIntegerDatatype()
//        val (newValue, warning) = f(value, format)
//        if (warning.nonEmpty) {
//          ("", "invalid_int")
//        } else if (
//          newValue.toInt > Int.MaxValue || newValue.toInt < Int.MinValue
//        )
//          ("", "invalid_int")
//        else (newValue, "")
      }
  }

  def processShortDatatype()
      : (String, Option[String]) => Either[String, Short] = { (value, format) =>
    {
      Left("hi")
//        val f = processIntegerDatatype()
//        val (newValue, warning) = f(value, format)

    }
  }

  def numericParser(
      value: String,
      format: Option[String]
  ): Either[String, Number] = {
    val numberFormatObject = NumberFormat(format, None, None)
    try {
      Right(numberFormatObject.parse(value))
    } catch {
      case e: NumberFormatError => Left(e.getMessage)
    }
    // what should be returned from this string or Number from NumberFormat class ??
  }

//  def createDateParser(
//      dateType: String,
//      warning: String
//  ): (String, String) => (String, String) = { (value, format) =>
//    {
//      val dateFormatObject = if (format.isEmpty) {
//        DateFormat(None, Some(dateType))
//      } else {
//        DateFormat(Some(format), None)
//      }
////      dateFormatObject.parse(value)
//    }
//  }

  def getOrdered(inheritedProperties: Map[String, JsonNode]): Boolean = {
    val inheritedPropertiesNode = inheritedProperties.get("ordered")
    inheritedPropertiesNode match {
      case Some(value) => value.asBoolean()
      case _           => false
    }
  }

  def getTextDirection(inheritedProperties: Map[String, JsonNode]): String = {
    val textDirectionNode = inheritedProperties.get("textDirection")
    textDirectionNode match {
      case Some(value) => value.asText()
      case _           => "inherit"
    }
  }

  def getSuppressOutput(columnProperties: Map[String, JsonNode]): Boolean = {
    val suppressOutputNode = columnProperties.get("suppressOutput")
    suppressOutputNode match {
      case Some(value) => value.asBoolean()
      case _           => false
    }
  }

  def getVirtual(columnProperties: Map[String, JsonNode]): Boolean = {
    val virtualNode = columnProperties.get("virtual")
    virtualNode match {
      case Some(value) => value.asBoolean()
      case _           => false
    }
  }

  def getRequired(inheritedProperties: Map[String, JsonNode]): Boolean = {
    inheritedProperties.get("required") match {
      case Some(value) => value.asBoolean()
      case _           => false
    }
  }

  def getDefault(inheritedProperties: Map[String, JsonNode]): String = {
    inheritedProperties.get("default") match {
      case Some(value) => value.asInstanceOf[TextNode].asText()
      case _           => ""
    }
  }

  def getId(columnProperties: Map[String, JsonNode]): Option[String] = {
    val idNode = columnProperties.get("@id")
    if (idNode.isDefined) Some(idNode.get.asText()) else None
  }

  def getName(
      columnProperties: Map[String, JsonNode],
      lang: String
  ): Option[String] = {
    val name = columnProperties.get("name")
    val titles = columnProperties.get("titles")

    if (name.isDefined) {
      Some(name.get.asInstanceOf[TextNode].asText())
    } else if (titles.isDefined && titles.get.path(lang).isMissingNode) {
      val langArray = Array.from(
        titles.get.path(lang).elements().asScala
      )
      if (langArray.nonEmpty) {
        Some(langArray(0).asText())
      } else None
    } else None // Not sure what to return here. Hope it does not reach here
  }

  def getNullParam(
      inheritedProperties: Map[String, JsonNode]
  ): Array[String] = {
    inheritedProperties.get("null") match {
      case Some(value) => {
        value match {
          case a: ArrayNode => {
            var nullParamsToReturn = Array[String]()
            val nullParams = Array.from(a.elements.asScala)
            for (np <- nullParams)
              nullParamsToReturn :+= np.asText()
            nullParamsToReturn
          }
          case s: TextNode => Array[String](s.asText())
          case _ =>
            throw new MetadataError("unexpected value for null property")
        }
      }
      case None => Array[String]("")
    }
  }

  def getAboutUrl(
      inheritedProperties: Map[String, JsonNode]
  ): Option[String] = {
    val aboutUrlNode = inheritedProperties.get("aboutUrl")
    aboutUrlNode match {
      case Some(value) => Some(value.asText())
      case _           => None
    }
  }

  def getPropertyUrl(
      inheritedProperties: Map[String, JsonNode]
  ): Option[String] = {
    val propertyUrlNode = inheritedProperties.get("propertyUrl")
    propertyUrlNode match {
      case Some(value) => Some(value.asText())
      case _           => None
    }
  }

  def getValueUrl(
      inheritedProperties: Map[String, JsonNode]
  ): Option[String] = {
    val valueUrlNode = inheritedProperties.get("valueUrl")
    valueUrlNode match {
      case Some(value) => Some(value.asText())
      case _           => None
    }
  }

  def getSeparator(
      inheritedProperties: Map[String, JsonNode]
  ): Option[String] = {
    val separatorNode = inheritedProperties.get("separator")
    separatorNode match {
      case Some(value) => Some(value.asText())
      case _           => None
    }
  }

  def partitionAndValidateColumnPropertiesByType(
      columnDesc: ObjectNode,
      columnOrdinal: Int,
      baseUrl: String,
      lang: String,
      inheritedProperties: Map[String, JsonNode]
  ): (Map[String, JsonNode], Map[String, JsonNode], Array[ErrorMessage]) = {
    var warnings = Array[ErrorMessage]()
    val annotations = Map[String, JsonNode]()
    val columnProperties = Map[String, JsonNode]()
    for ((property, value) <- columnDesc.getKeysAndValues) {
      (property, value) match {
        case ("@type", v: TextNode) if v.asText != "Column" => {
          throw new MetadataError(
            s"columns[$columnOrdinal].@type, @type of column is not 'Column'"
          )
        }
        case _ => {
          val (v, w, csvwPropertyType) =
            PropertyChecker.checkProperty(property, value, baseUrl, lang)
          warnings = warnings.concat(
            w.map(warningString =>
              ErrorMessage(
                warningString,
                "metadata",
                "",
                s"$columnOrdinal",
                s"$property: ${value.toPrettyString}",
                ""
              )
            )
          )
          csvwPropertyType match {
            case PropertyType.Inherited =>
              inheritedProperties += (property -> v)
            case PropertyType.Common | PropertyType.Column =>
              columnProperties += (property -> v)
            case PropertyType.Annotation => {
              annotations += (property -> v)
            }
            case _ =>
              warnings :+= ErrorMessage(
                s"invalid_property",
                "metadata",
                "",
                "",
                s"column: ${property}",
                ""
              )
          }
        }
      }
    }
    (annotations, columnProperties, warnings)
  }

  def fromJson(
      columnOrdinal: Int,
      columnDesc: ObjectNode,
      baseUrl: String,
      lang: String,
      inheritedProperties: Map[String, JsonNode]
  ): Column = {

    val inheritedPropertiesCopy =
      MapHelpers.deepCloneJsonPropertiesMap(inheritedProperties)

    var (annotations, columnProperties, warnings) =
      partitionAndValidateColumnPropertiesByType(
        columnDesc,
        columnOrdinal,
        baseUrl,
        lang,
        inheritedPropertiesCopy
      )
    val datatype = getDatatypeOrDefault(inheritedPropertiesCopy)

    val newLang = getLangOrDefault(inheritedPropertiesCopy)

    new Column(
      columnOrdinal = columnOrdinal,
      name = getName(columnProperties, lang),
      id = getId(columnProperties),
      datatype = datatype,
      lang = newLang,
      nullParam = getNullParam(inheritedPropertiesCopy),
      default = getDefault(inheritedPropertiesCopy),
      required = getRequired(inheritedPropertiesCopy),
      aboutUrl = getAboutUrl(inheritedPropertiesCopy),
      propertyUrl = getPropertyUrl(inheritedPropertiesCopy),
      valueUrl = getValueUrl(inheritedPropertiesCopy),
      separator = getSeparator(inheritedPropertiesCopy),
      ordered = getOrdered(inheritedPropertiesCopy),
      titles = columnProperties.get("titles"),
      suppressOutput = getSuppressOutput(columnProperties),
      virtual = getVirtual(columnProperties),
      textDirection = getTextDirection(inheritedPropertiesCopy),
      annotations = annotations,
      warnings = warnings,
      errors = Array()
    )
  }

  private def getLangOrDefault(
      inheritedPropertiesCopy: Map[String, JsonNode]
  ): String = {
    inheritedPropertiesCopy.get("lang") match {
      case Some(lang) => lang.asText()
      case _          => "und"
    }
  }

  private def getDatatypeOrDefault(
      inheritedPropertiesCopy: Map[String, JsonNode]
  ): JsonNode = {
    inheritedPropertiesCopy.get("datatype") match {
      case Some(datatype) => datatype
      case _              => datatypeDefaultValue
    }
  }

  def languagesMatch(l1: String, l2: String): Boolean = {
    val languagesMatchOrEitherIsUndefined =
      l1 == l2 || l1 == "und" || l2 == "und"
    val oneLanguageIsSubClassOfAnother =
      l1.startsWith(s"$l2-") || l2.startsWith(s"$l1-")

    languagesMatchOrEitherIsUndefined || oneLanguageIsSubClassOfAnother
  }

}

case class Column private (
    columnOrdinal: Int,
    name: Option[String],
    id: Option[String],
    aboutUrl: Option[String],
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
    titles: Option[JsonNode],
    valueUrl: Option[String],
    virtual: Boolean,
    annotations: Map[String, JsonNode],
    warnings: Array[ErrorMessage],
    var errors: Array[ErrorMessage]
) {
  def addErrorIfRequiredValueAndValueEmpty(
      value: String,
      rowNumber: Long
  ): Unit = {
    if (required && value.isEmpty) {
      errors :+= ErrorMessage(
        "Required",
        "schema",
        rowNumber.toString,
        columnOrdinal.toString,
        value,
        s"required => $required"
      )
    }
  }

  def validate(value: String, rowNumber: Long) = {
    if (nullParam.contains(value)) {
      addErrorIfRequiredValueAndValueEmpty(value, rowNumber)
    } else {
      val newValue = separator match {
        case Some(separator) => value.split(separator)
        case None            => Array[String](value)
      }
      for (s <- newValue) {

//        val (value, warning) = //CREATE DATATYPE PARSERS
//        val format = ""
//        val (value, warning) = Column.DatatypeParser(s)(value, format)
      }

    }
  }

  def validateHeader(columnName: String): WarningsAndErrors = {
    var errors = Array[ErrorMessage]()
    titles match {
      case Some(titles) => {
        var validHeaders = Array[String]()
        for ((_, v) <- titles.asInstanceOf[ObjectNode].getKeysAndValues) {
          val titlesArray = Array.from(v.elements().asScala)
          for (title <- titlesArray) {
            val titleString = title.asText()
            if (Column.languagesMatch(titleString, lang)) {
              validHeaders :+= titleString
            }
          }
        }
        if (!validHeaders.contains(columnName)) {
          errors :+= ErrorMessage(
            "Invalid Header",
            "Schema",
            "1",
            columnOrdinal.toString,
            columnName,
            titles.toPrettyString
          )
        }
        WarningsAndErrors(Array(), errors)
      }
      case None => WarningsAndErrors(Array(), Array())
    }
  }
}
