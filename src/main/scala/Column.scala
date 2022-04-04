package CSVValidation

import CSVValidation.Column.datatypeParser
import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import com.ibm.icu

import java.lang
import java.math.BigInteger
import java.time.ZonedDateTime
import scala.collection.mutable.Map
import scala.jdk.CollectionConverters.IteratorHasAsScala
import scala.math.BigInt.javaBigInteger2bigInt

object Column {
  val datatypeDefaultValue: ObjectNode = JsonNodeFactory.instance
    .objectNode()
    .set("@id", new TextNode("http://www.w3.org/2001/XMLSchema#string"))
  val rdfSyntaxNs = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  val xmlSchema = "http://www.w3.org/2001/XMLSchema#"

  val datatypeParser = Map(
    s"${rdfSyntaxNs}XMLLiteral" -> trimValue _,
    s"${rdfSyntaxNs}HTML" -> trimValue _,
    "http://www.w3.org/ns/csvw#JSON" -> trimValue _,
    s"${xmlSchema}anyAtomicType" -> allValueValid _,
    s"${xmlSchema}anyURI" -> trimValue _,
    s"${xmlSchema}base64Binary" -> trimValue _,
    s"${xmlSchema}hexBinary" -> trimValue _,
    s"${xmlSchema}QName" -> trimValue _,
    s"${xmlSchema}string" -> allValueValid _,
    s"${xmlSchema}normalizedString" -> trimValue _,
    s"${xmlSchema}token" -> trimValue _,
    s"${xmlSchema}language" -> trimValue _,
    s"${xmlSchema}Name" -> trimValue _,
    s"${xmlSchema}NMTOKEN" -> trimValue _,
    s"${xmlSchema}boolean" -> processBooleanDatatype _,
    s"${xmlSchema}decimal" -> processDecimalDatatype _,
    s"${xmlSchema}integer" -> processIntegerDatatype _,
    s"${xmlSchema}long" -> processLongDatatype _,
    s"${xmlSchema}int" -> processIntDatatype _,
    s"${xmlSchema}short" -> processShortDatatype _,
    s"${xmlSchema}byte" -> processByteDatatype _,
    s"${xmlSchema}nonNegativeInteger" -> processNonNegativeInteger _,
    s"${xmlSchema}positiveInteger" -> processPositiveInteger _,
    s"${xmlSchema}unsignedLong" -> processUnsignedLong _,
    s"${xmlSchema}unsignedInt" -> processUnsignedInt _,
    s"${xmlSchema}unsignedShort" -> processUnsignedShort _,
    s"${xmlSchema}unsignedByte" -> processUnsignedByte _,
    s"${xmlSchema}nonPositiveInteger" -> processNonPositiveInteger _,
    s"${xmlSchema}negativeInteger" -> processNegativeInteger _,
    s"${xmlSchema}double" -> processDoubleDatatype _,
    s"${xmlSchema}float" -> processFloatDatatype _,
    // Date Time related datatype
    s"${xmlSchema}date" -> processDateDatatype _,
    s"${xmlSchema}dateTime" -> processDateTimeDatatype _,
    s"${xmlSchema}dateTimeStamp" -> processDateTimeStamp _,
    s"${xmlSchema}gDay" -> processGDay _,
    s"${xmlSchema}gMonth" -> processGMonth _,
    s"${xmlSchema}gMonthDay" -> processGMonthDay _,
    s"${xmlSchema}gYear" -> processGYear _,
    s"${xmlSchema}gYearMonth" -> processGYearMonth _,
    s"${xmlSchema}time" -> processTime _
  )

  val validDecimalDatatypeRegex =
    "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)".r

  val validDoubleDatatypeRegex =
    "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN".r

  val validFloatDatatypeRegex =
    "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN".r

  val validIntegerRegex = "[\\-+]?[0-9]+".r

  val validLongDatatypeRegex = "[\\-+]?[0-9]+".r

  def trimValue(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, String] = Right(value.strip())

  def allValueValid(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, String] = {
    Right(value)
  }

  def processBooleanDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Boolean] = {
    maybeFormat match {
      case Some(f) => {
        var formatValues = Array[String]()
        formatValues = f.split("""\|""")
        if (formatValues(0) == value) {
          Right(true)
        } else if (formatValues(1) == value) {
          Right(false)
        } else Left("invalid_boolean")
      }
      case None => {
        if (Array[String]("true", "1").contains(value)) {
          Right(true)
        } else if (Array[String]("false", "0").contains(value)) {
          Right(false)
        } else Left("invalid_boolean")
      }
    }
  }

  def processDecimalDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Double] = {
    if (!validDecimalDatatypeRegex.pattern.matcher(value).matches()) {
      Left("invalid_decimal")
    } else {
      numericParser(value, maybeFormat) match {
        case Left(_)            => Left("invalid_decimal")
        case Right(parsedValue) => Right(parsedValue.doubleValue())
      }
    }
  }

  def processDoubleDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Double] = {
    if (!validDoubleDatatypeRegex.pattern.matcher(value).matches()) {
      Left("invalid_double")
    } else {
      numericParser(value, maybeFormat) match {
        case Left(_)            => Left("invalid_double")
        case Right(parsedValue) => Right(parsedValue.doubleValue())
      }
    }
  }

  def processFloatDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Float] = {
    if (!validFloatDatatypeRegex.pattern.matcher(value).matches()) {
      Left("invalid_float")
    } else {
      numericParser(value, maybeFormat) match {
        case Left(_)            => Left("invalid_float")
        case Right(parsedValue) => Right(parsedValue.floatValue())
      }
    }
  }

  def processIntegerDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, BigInteger] = {
    if (!validIntegerRegex.pattern.matcher(value).matches()) {
      Left("invalid_integer")
    } else {
      numericParser(value, maybeFormat) match {
        case Right(parsedValue) => convertToBigIntegerValue(value, parsedValue)
        case Left(warning) =>
          Left(s"invalid_integer - '${value}' ${warning}")
      }
    }
  }

  private def convertToBigIntegerValue(
      value: String,
      parsedValue: Number
  ): Either[String, BigInteger] = {
    try {
      val bigIntValue = parsedValue match {
        case _: lang.Long | _: Integer | _: lang.Short =>
          BigInteger.valueOf(parsedValue.longValue())
        case bigDecimalValue: icu.math.BigDecimal =>
          bigDecimalValue.toBigIntegerExact
        case _ =>
          throw new IllegalArgumentException(
            s"Unexpected type ${parsedValue.getClass}"
          )
      }
      Right(bigIntValue)
    } catch {
      case e => Left(s"invalid_integer - '$value' - ${e.getMessage}")
    }
  }

  def processLongDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Long] = {
    if (!validLongDatatypeRegex.pattern.matcher(value).matches()) {
      Left("invalid_long")
    } else {
      processIntegerDatatype(value, maybeFormat) match {
        case Left(warning) => Left(s"invalid_long - ${warning}")
        case Right(parsedValue) => {
          if (parsedValue > Long.MaxValue || parsedValue < Long.MinValue) {
            Left(s"invalid_long - '$value' Outside Long Range")
          } else Right(parsedValue.longValue())
        }
      }
    }
  }

  def processIntDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Int] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_int")
      case Right(parsedValue) => {
        if (parsedValue > Int.MaxValue || parsedValue < Int.MinValue)
          Left(s"invalid_int - '$value' Outside Int Range")
        else Right(parsedValue.intValue())
      }
    }
  }

  def processShortDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Short] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_short")
      case Right(parsedValue) => {
        if (parsedValue > Short.MaxValue || parsedValue < Short.MinValue) {
          Left("invalid_short")
        } else Right(parsedValue.shortValue())
      }
    }
  }

  def processByteDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Byte] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_byte")
      case Right(parsedValue) => {
        if (parsedValue > Byte.MaxValue || parsedValue < Byte.MinValue) {
          Left("invalid_byte")
        } else Right(parsedValue.byteValue())
      }
    }
  }

  def processNonNegativeInteger(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, BigInteger] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_nonNegativeInteger")
      case Right(parsedValue) => {
        if (parsedValue < 0) {
          Left("invalid_nonNegativeInteger")
        } else Right(parsedValue)
      }
    }
  }

  def processPositiveInteger(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, BigInteger] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_positiveInteger")
      case Right(parsedValue) => {
        if (parsedValue <= 0) {
          Left("invalid_positiveInteger")
        } else Right(parsedValue)
      }
    }
  }

  def processUnsignedLong(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, BigInteger] = {
    val result = processNonNegativeInteger(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_unsignedLong")
      case Right(parsedValue) => {
        val unsignedLongMaxValue: BigInt = BigInt("18446744073709551615")
        if (parsedValue > unsignedLongMaxValue) {
          Left("invalid_unsignedLong")
        } else Right(parsedValue)
      }
    }
  }

  def processUnsignedInt(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Long] = {
    val result = processNonNegativeInteger(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_unsignedInt")
      case Right(parsedValue) => {
        if (parsedValue > 4294967295L) {
          Left("invalid_unsignedInt")
        } else Right(parsedValue.longValue())
      }
    }
  }

  def processUnsignedShort(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Long] = {
    val result = processNonNegativeInteger(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_unsignedShort")
      case Right(parsedValue) => {
        if (parsedValue > 65535) {
          Left("invalid_unsignedShort")
        } else Right(parsedValue.intValue())
      }
    }
  }

  def processUnsignedByte(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, Short] = {
    val result = processNonNegativeInteger(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_unsignedByte")
      case Right(parsedValue) => {
        if (parsedValue > 255) {
          Left("invalid_unsignedByte")
        } else Right(parsedValue.shortValue())
      }
    }
  }

  def processNonPositiveInteger(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, BigInteger] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) => Left("invalid_nonPositiveInteger")
      case Right(parsedValue) => {
        if (parsedValue > 0) {
          Left("invalid_nonPositiveInteger")
        } else Right(parsedValue)
      }
    }
  }

  def processNegativeInteger(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, BigInteger] = {
    val result = processIntegerDatatype(value, maybeFormat)
    result match {
      case Left(_) =>
        Left(
          "invalid_negativeInteger"
        ) // Add the original value in warnings
      case Right(parsedValue) => {
        if (parsedValue >= 0) {
          Left("invalid_negativeInteger")
        } else Right(parsedValue)
      }
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
  }

  def processDateDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}date",
      "invalid_date",
      value,
      maybeFormat
    )
  }

  def processDateTimeDatatype(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}dateTime",
      "invalid_datetime",
      value,
      maybeFormat
    )
  }

  def processDateTimeStamp(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}dateTimeStamp",
      "invalid_dateTimeStamp",
      value,
      maybeFormat
    )
  }

  def processGDay(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gDay",
      "invalid_gDay",
      value,
      maybeFormat
    )
  }

  def processGMonth(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gMonth",
      "invalid_gMonth",
      value,
      maybeFormat
    )
  }

  def processGMonthDay(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gMonthDay",
      "invalid_gMonthDat",
      value,
      maybeFormat
    )
  }

  def processGYear(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gYear",
      "invalid_gYear",
      value,
      maybeFormat
    )
  }
  def processGYearMonth(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gYearMonth",
      "invalid_gYearMonth",
      value,
      maybeFormat
    )
  }

  def processTime(
      value: String,
      maybeFormat: Option[String]
  ): Either[String, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}time",
      "invalid_time",
      value,
      maybeFormat
    )
  }

  def dateTimeParser(
      datatype: String,
      warning: String,
      value: String,
      format: Option[String]
  ): Either[String, ZonedDateTime] = {
    val dateFormatObject = DateFormat(format, datatype)
    dateFormatObject.parse(value) match {
      case Some(parsedValue) => Right(parsedValue)
      case None              => Left(warning)
    }
  }

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

  def validate(
      value: String,
      rowNumber: Long
  ) = {
    if (nullParam.contains(value)) {
      addErrorIfRequiredValueAndValueEmpty(value, rowNumber)
    } else {
      val newValue = separator match {
        case Some(separator) => value.split(separator)
        case None            => Array[String](value)
      }
      val datatypeBaseField = datatype.path("base")
      val datatypeOfColumn = if (datatypeBaseField.isMissingNode) {
        datatype.get("@id").asText()
      } else {
        datatypeBaseField.asText()
      }
      val datatypeFormatField = datatype.path("format")
      val datatypeFormat: Option[String] =
        if (datatypeFormatField.isMissingNode) {
          None
        } else {
          Some(datatypeFormatField.asText())
        }
      for (s <- newValue) {
        val result =
          datatypeParser(datatypeOfColumn)(s, datatypeFormat)
        if (result.isLeft) {
          errors = errors :+ ErrorMessage(
            result.swap.getOrElse("datatype_validation_failed"),
            "schema",
            rowNumber.toString,
            columnOrdinal.toString,
            s,
            datatype.toPrettyString
          )
        }
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
