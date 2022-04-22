package CSVValidation

import CSVValidation.Column.{
  rdfSyntaxNs,
  unsignedLongMaxValue,
  validDecimalDatatypeRegex,
  validDoubleDatatypeRegex,
  xmlSchema
}
import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import com.ibm.icu
import errors.ErrorWithoutContext

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

  val validDecimalDatatypeRegex =
    "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)".r

  val validDoubleDatatypeRegex, validFloatDatatypeRegex =
    "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN".r

  val validIntegerRegex = "[\\-+]?[0-9]+".r

  val validLongDatatypeRegex = "[\\-+]?[0-9]+".r

  val unsignedLongMaxValue: BigInt = BigInt("18446744073709551615")

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

  def getTitleValues(
      titles: Option[JsonNode]
  ): Array[String] = {
    var validHeaders = Array[String]()
    titles match {
      case Some(titles) => {
        for ((_, v) <- titles.asInstanceOf[ObjectNode].getKeysAndValues) {
          val titlesArray = Array.from(v.elements().asScala)
          for (title <- titlesArray) {
            validHeaders :+= title.asText()
          }
        }
        validHeaders
      }
      case _ => validHeaders
    }
  }

  def getBaseDataType(datatype: JsonNode): String = {
    val datatypeBaseField = datatype.path("base")
    if (datatypeBaseField.isMissingNode) {
      datatype.get("@id").asText()
    } else {
      datatypeBaseField.asText()
    }
  }

  def partitionAndValidateColumnPropertiesByType(
      columnDesc: ObjectNode,
      columnOrdinal: Int,
      baseUrl: String,
      lang: String,
      inheritedProperties: Map[String, JsonNode]
  ): (
      Map[String, JsonNode],
      Map[String, JsonNode],
      Array[ErrorWithCsvContext]
  ) = {
    var warnings = Array[ErrorWithCsvContext]()
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
              ErrorWithCsvContext(
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
              warnings :+= ErrorWithCsvContext(
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

    val formatNode = columnDesc.path("format")

    val titles = columnProperties.get("titles")

    new Column(
      columnOrdinal = columnOrdinal,
      name = getName(columnProperties, lang),
      id = getId(columnProperties),
      datatype = datatype,
      baseDataType = getBaseDataType(datatype),
      lang = newLang,
      nullParam = getNullParam(inheritedPropertiesCopy),
      default = getDefault(inheritedPropertiesCopy),
      required = getRequired(inheritedPropertiesCopy),
      aboutUrl = getAboutUrl(inheritedPropertiesCopy),
      propertyUrl = getPropertyUrl(inheritedPropertiesCopy),
      valueUrl = getValueUrl(inheritedPropertiesCopy),
      separator = getSeparator(inheritedPropertiesCopy),
      ordered = getOrdered(inheritedPropertiesCopy),
      titles = titles,
      titleValues = getTitleValues(titles),
      suppressOutput = getSuppressOutput(columnProperties),
      virtual = getVirtual(columnProperties),
      format = getMaybeFormatForColumn(formatNode),
      textDirection = getTextDirection(inheritedPropertiesCopy),
      annotations = annotations,
      warnings = warnings,
      errors = Array()
    )
  }

  private def getMaybeFormatForColumn(formatNode: JsonNode): Option[Format] = {
    if (formatNode.isMissingNode || formatNode.isNull) {
      None
    } else {
      formatNode match {
        case s: TextNode => Some(Format(Some(s.asText()), None, None))
        case _ => {
          val formatObjectNode = formatNode.asInstanceOf[ObjectNode]

          def getMaybeValueFromNode(
              node: ObjectNode,
              propertyName: String
          ): Option[String] = {
            if (node.isMissingNode) {
              None
            } else {
              Some(node.get(propertyName).asText())
            }
          }

          val pattern = getMaybeValueFromNode(formatObjectNode, "pattern")
          val decimalChar =
            getMaybeValueFromNode(formatObjectNode, "decimalChar")
              .map(d => d(0))
          val groupChar = getMaybeValueFromNode(formatObjectNode, "groupChar")
            .map(d => d(0))

          Some(Format(pattern, decimalChar, groupChar))
        }
      }
    }
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
    baseDataType: String,
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
    titleValues: Array[String],
    valueUrl: Option[String],
    virtual: Boolean,
    format: Option[Format],
    annotations: Map[String, JsonNode],
    warnings: Array[ErrorWithCsvContext],
    var errors: Array[ErrorWithCsvContext]
) {
  val datatypeParser: Map[String, String => Either[
    ErrorWithoutContext,
    Any
  ]] =
    Map(
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

  def trimValue(
      value: String
  ): Either[ErrorWithoutContext, String] = Right(value.strip())

  def allValueValid(
      value: String
  ): Either[ErrorWithoutContext, String] = {
    Right(value)
  }

  def processBooleanDatatype(
      value: String
  ): Either[ErrorWithoutContext, Boolean] = {
    format.flatMap(f => f.pattern) match {
      case Some(pattern) => {
        var patternValues = pattern.split("""\|""")
        if (patternValues(0) == value) {
          Right(true)
        } else if (patternValues(1) == value) {
          Right(false)
        } else
          Left(
            ErrorWithoutContext(
              "invalid_boolean",
              "Not in list of expected values"
            )
          )
      }
      case None => {
        if (Array[String]("true", "1").contains(value)) {
          Right(true)
        } else if (Array[String]("false", "0").contains(value)) {
          Right(false)
        } else
          Left(
            ErrorWithoutContext(
              "invalid_boolean",
              "Not in default expected values (true/false/1/0)"
            )
          )
      }
    }
  }

  def processDecimalDatatype(
      value: String
  ): Either[ErrorWithoutContext, BigDecimal] = {
    if (!validDecimalDatatypeRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_decimal",
          "Does not match expected Decimal format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w) => Left(ErrorWithoutContext("invalid_decimal", w))
        case Right(parsedValue) => {
          parsedValue match {
            case bigD: icu.math.BigDecimal => Right(bigD.toBigDecimal)
            case _                         => Right(BigDecimal(parsedValue.longValue()))
          }
        }
      }
    }
  }

  def processDoubleDatatype(
      value: String
  ): Either[ErrorWithoutContext, Double] = {
    if (!validDoubleDatatypeRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_double",
          "Does not match expected Double format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w)            => Left(ErrorWithoutContext("invalid_double", w))
        case Right(parsedValue) => Right(parsedValue.doubleValue())
      }
    }
  }

  def processFloatDatatype(
      value: String
  ): Either[ErrorWithoutContext, Float] = {
    if (!Column.validFloatDatatypeRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_float",
          "Does not match expected Float format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w)            => Left(ErrorWithoutContext("invalid_float", w))
        case Right(parsedValue) => Right(parsedValue.floatValue())
      }
    }
  }

  def processIntegerDatatype(
      value: String
  ): Either[ErrorWithoutContext, BigInteger] = {
    if (!Column.validIntegerRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_integer",
          "Does not match expected integer format"
        )
      )
    } else {
      numericParser(value) match {
        case Right(parsedValue) => convertToBigIntegerValue(parsedValue)
        case Left(warning) =>
          Left(ErrorWithoutContext("invalid_integer", warning))
      }
    }
  }

  private def convertToBigIntegerValue(
      parsedValue: Number
  ): Either[ErrorWithoutContext, BigInteger] = {
    try {
      val bigIntValue = parsedValue match {
        case _: java.lang.Long | _: Integer | _: java.lang.Short =>
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
      case e =>
        Left(ErrorWithoutContext("invalid_integer", e.getMessage))
    }
  }

  def processLongDatatype(
      value: String
  ): Either[ErrorWithoutContext, Long] = {
    if (!Column.validLongDatatypeRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_long",
          "Does not match expected long format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w) => Left(ErrorWithoutContext("invalid_long", w))
        case Right(parsedValue) => {
          parsedValue match {
            case _: java.lang.Long | _: java.lang.Integer | _: java.lang.Short |
                _: java.lang.Byte =>
              Right(parsedValue.longValue())
            case _ =>
              Left(
                ErrorWithoutContext(
                  "invalid_long",
                  s"Outside long range ${Long.MinValue} - ${Long.MaxValue} (Inclusive)"
                )
              )
          }
        }
      }
    }
  }

  def processIntDatatype(
      value: String
  ): Either[ErrorWithoutContext, Int] = {
    if (!Column.validIntegerRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_int",
          "Does not match expected integer format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w) => Left(ErrorWithoutContext("invalid_int", w))
        case Right(parsedNumber) => {
          parsedNumber match {
            case _: java.lang.Long | _: java.lang.Integer | _: java.lang.Short |
                _: java.lang.Byte => {
              val parsedValue = parsedNumber.longValue()
              if (parsedValue > Int.MaxValue || parsedValue < Int.MinValue)
                Left(
                  ErrorWithoutContext(
                    "invalid_int",
                    s"Outside Int Range ${Int.MinValue} - ${Int.MaxValue} (inclusive)"
                  )
                )
              else Right(parsedValue.intValue())
            }
            case _ =>
              Left(
                ErrorWithoutContext(
                  "invalid_int",
                  s"Outside Int Range ${Int.MinValue} - ${Int.MaxValue} (inclusive)"
                )
              )
          }
        }
      }
    }
  }

  def processShortDatatype(
      value: String
  ): Either[ErrorWithoutContext, Short] = {
    if (!Column.validIntegerRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_short",
          "Does not match expected short format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w) => Left(ErrorWithoutContext("invalid_short", w))
        case Right(parsedNumber) => {
          parsedNumber match {
            case _: java.lang.Long | _: java.lang.Integer | _: java.lang.Short |
                _: java.lang.Byte => {
              val parsedValue = parsedNumber.longValue()
              if (parsedValue > Short.MaxValue || parsedValue < Short.MinValue)
                Left(
                  ErrorWithoutContext(
                    "invalid_short",
                    s"Outside Short Range ${Short.MinValue} - ${Short.MaxValue} (inclusive)"
                  )
                )
              else Right(parsedValue.shortValue())
            }
            case _ =>
              Left(
                ErrorWithoutContext(
                  "invalid_short",
                  s"Outside Short Range ${Short.MinValue} - ${Short.MaxValue} (inclusive)"
                )
              )
          }
        }
      }
    }
  }

  def processByteDatatype(
      value: String
  ): Either[ErrorWithoutContext, Byte] = {
    if (!Column.validIntegerRegex.pattern.matcher(value).matches()) {
      Left(
        ErrorWithoutContext(
          "invalid_byte",
          "Does not match expected byte format"
        )
      )
    } else {
      numericParser(value) match {
        case Left(w) => Left(ErrorWithoutContext("invalid_byte", w))
        case Right(parsedNumber) => {
          parsedNumber match {
            case _: java.lang.Long | _: java.lang.Integer | _: java.lang.Short |
                _: java.lang.Byte => {
              val parsedValue = parsedNumber.byteValue()
              if (parsedValue > Byte.MaxValue || parsedValue < Byte.MinValue)
                Left(
                  ErrorWithoutContext(
                    "invalid_byte",
                    s"Outside Byte Range ${Byte.MinValue} - ${Byte.MaxValue} (inclusive)"
                  )
                )
              else Right(parsedValue.byteValue())
            }
            case _ =>
              Left(
                ErrorWithoutContext(
                  "invalid_byte",
                  s"Outside Byte Range ${Byte.MinValue} - ${Byte.MaxValue} (inclusive)"
                )
              )
          }
        }
      }
    }
  }

  def processNonNegativeInteger(
      value: String
  ): Either[ErrorWithoutContext, BigInteger] = {
    val result = processIntegerDatatype(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_nonNegativeInteger", w.content))
      case Right(parsedValue) => {
        if (parsedValue < 0) {
          Left(
            ErrorWithoutContext(
              "invalid_nonNegativeInteger",
              "Value less than 0"
            )
          )
        } else Right(parsedValue)
      }
    }
  }

  def processPositiveInteger(
      value: String
  ): Either[ErrorWithoutContext, BigInteger] = {
    val result = processIntegerDatatype(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_positiveInteger", w.content))
      case Right(parsedValue) => {
        if (parsedValue <= 0) {
          Left(
            ErrorWithoutContext(
              "invalid_positiveInteger",
              "Value less than or equal to 0"
            )
          )
        } else Right(parsedValue)
      }
    }
  }

  def processUnsignedLong(
      value: String
  ): Either[ErrorWithoutContext, BigInteger] = {
    val result = processNonNegativeInteger(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_unsignedLong", w.content))
      case Right(parsedValue) => {
        if (parsedValue > unsignedLongMaxValue) {
          Left(
            ErrorWithoutContext(
              "invalid_unsignedLong",
              "Value greater than 18446744073709551615"
            )
          )
        } else Right(parsedValue)
      }
    }
  }

  def processUnsignedInt(
      value: String
  ): Either[ErrorWithoutContext, Long] = {
    val result = processNonNegativeInteger(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_unsignedInt", w.content))
      case Right(parsedValue) => {
        if (parsedValue > 4294967295L) {
          Left(
            ErrorWithoutContext(
              "invalid_unsignedInt",
              "Value greater than 4294967295"
            )
          )
        } else Right(parsedValue.longValue())
      }
    }
  }

  def processUnsignedShort(
      value: String
  ): Either[ErrorWithoutContext, Long] = {
    val result = processNonNegativeInteger(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_unsignedShort", w.content))
      case Right(parsedValue) => {
        if (parsedValue > 65535) {
          Left(
            ErrorWithoutContext(
              "invalid_unsignedShort",
              "Value greater than 65535"
            )
          )
        } else Right(parsedValue.intValue())
      }
    }
  }

  def processUnsignedByte(
      value: String
  ): Either[ErrorWithoutContext, Short] = {
    val result = processNonNegativeInteger(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_unsignedByte", w.content))
      case Right(parsedValue) => {
        if (parsedValue > 255) {
          Left(ErrorWithoutContext("invalid_unsignedByte", "Greater than 255"))
        } else Right(parsedValue.shortValue())
      }
    }
  }

  def processNonPositiveInteger(
      value: String
  ): Either[ErrorWithoutContext, BigInteger] = {
    val result = processIntegerDatatype(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_nonPositiveInteger", w.content))
      case Right(parsedValue) => {
        if (parsedValue > 0) {
          Left(
            ErrorWithoutContext(
              "invalid_nonPositiveInteger",
              "Parsed value greater than 0"
            )
          )
        } else Right(parsedValue)
      }
    }
  }

  def processNegativeInteger(
      value: String
  ): Either[ErrorWithoutContext, BigInteger] = {
    val result = processIntegerDatatype(value)
    result match {
      case Left(w) =>
        Left(ErrorWithoutContext("invalid_negativeInteger", w.content))
      case Right(parsedValue) => {
        if (parsedValue >= 0) {
          Left(
            ErrorWithoutContext(
              "invalid_negativeInteger",
              "Value greater than 0"
            )
          )
        } else Right(parsedValue)
      }
    }
  }

  def numericParser(
      value: String
  ): Either[String, Number] = {
    val numberFormatObject =
      NumberFormat(
        format.flatMap(f => f.pattern),
        format.flatMap(f => f.groupChar),
        format.flatMap(f => f.decimalChar)
      )
    try {
      Right(numberFormatObject.parse(value))
    } catch {
      case e: NumberFormatError => Left(e.getMessage)
    }
  }

  def processDateDatatype(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}date",
      "invalid_date",
      value
    )
  }

  def processDateTimeDatatype(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}dateTime",
      "invalid_datetime",
      value
    )
  }

  def processDateTimeStamp(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}dateTimeStamp",
      "invalid_dateTimeStamp",
      value
    )
  }

  def processGDay(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gDay",
      "invalid_gDay",
      value
    )
  }

  def processGMonth(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gMonth",
      "invalid_gMonth",
      value
    )
  }

  def processGMonthDay(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gMonthDay",
      "invalid_gMonthDat",
      value
    )
  }

  def processGYear(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gYear",
      "invalid_gYear",
      value
    )
  }
  def processGYearMonth(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}gYearMonth",
      "invalid_gYearMonth",
      value
    )
  }

  def processTime(
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    dateTimeParser(
      s"${xmlSchema}time",
      "invalid_time",
      value
    )
  }

  def dateTimeParser(
      datatype: String,
      warning: String,
      value: String
  ): Either[ErrorWithoutContext, ZonedDateTime] = {
    val dateFormatObject = DateFormat(format.flatMap(f => f.pattern), datatype)
    dateFormatObject.parse(value) match {
      case Right(value) => Right(value)
      case Left(error)  => Left(ErrorWithoutContext(warning, error))
    }
  }

  def addErrorIfRequiredValueAndValueEmpty(
      value: String,
      rowNumber: Long
  ): Unit = {
    if (required && value.isEmpty) {
      errors :+= ErrorWithCsvContext(
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
  ): WarningsAndErrors = {
    if (nullParam.contains(value)) {
      addErrorIfRequiredValueAndValueEmpty(value, rowNumber)
    } else {
      val values = separator match {
        case Some(separator) => value.split(separator)
        case None            => Array[String](value)
      }
      val parserForDataType = datatypeParser(baseDataType)
      for (v <- values) {
        parserForDataType(v) match {
          case Left(errorMessageContent) => {
            errors = errors :+ ErrorWithCsvContext(
              errorMessageContent.`type`,
              "schema",
              rowNumber.toString,
              columnOrdinal.toString,
              s"'$v' - ${errorMessageContent.content}",
              datatype.toPrettyString
            )
          }
          case Right(s) => {}
        }
      }
    }
    WarningsAndErrors(errors, warnings)
  }

  def validateHeader(columnName: String): WarningsAndErrors = {
    var errors = Array[ErrorWithCsvContext]()
    titles match {
      case Some(titles) => {
        var validHeaders = Array[String]()
        for (title <- titleValues) {
          if (Column.languagesMatch(title, lang)) {
            validHeaders :+= title
          }
        }
        if (!validHeaders.contains(columnName)) {
          errors :+= ErrorWithCsvContext(
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
