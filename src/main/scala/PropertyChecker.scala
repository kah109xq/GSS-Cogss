package CSVValidation
import com.fasterxml.jackson.databind.node._
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.joda.time.DateTime
import CSVValidation.traits.ObjectNodeExtentions.ObjectNodeGetMaybeNode

import java.net.{URI, URL}
import scala.jdk.CollectionConverters._
import scala.util.matching.Regex

object PropertyChecker {
  val startsWithUnderscore = "^_:.*$".r
  val containsColon = ".*:.*".r
  val mapper = new ObjectMapper
  val invalidValueWarning = "invalid_value"
  val Bcp47Regular =
    "(art-lojban|cel-gaulish|no-bok|no-nyn|zh-guoyu|zh-hakka|zh-min|zh-min-nan|zh-xiang)"
  val Bcp47Irregular =
    "(en-GB-oed|i-ami|i-bnn|i-default|i-enochian|i-hak|i-klingon|i-lux|i-mingo|i-navajo|i-pwn|i-tao|i-tay|i-tsu|sgn-BE-FR|sgn-BE-NL|sgn-CH-DE)"
  val Bcp47Grandfathered =
    "(?<grandfathered>" + Bcp47Irregular + "|" + Bcp47Regular + ")"
  val Bcp47PrivateUse = "(x(-[A-Za-z0-9]{1,8})+)"
  val Bcp47Singleton = "[0-9A-WY-Za-wy-z]"
  val Bcp47Extension =
    "(?<extension>" + Bcp47Singleton + "(-[A-Za-z0-9]{2,8})+)"
  val Bcp47Variant = "(?<variant>[A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3})"
  val Bcp47Region = "(?<region>[A-Za-z]{2}|[0-9]{3})"
  val Bcp47Script = "(?<script>[A-Za-z]{4})"
  val Bcp47Extlang = "(?<extlang>[A-Za-z]{3}(-[A-Za-z]{3}){0,2})"
  val Bcp47Language =
    "(?<language>([A-Za-z]{2,3}(-" + Bcp47Extlang + ")?)|[A-Za-z]{4}|[A-Za-z]{5,8})"
  val Bcp47Langtag =
    "(" + Bcp47Language + "(-" + Bcp47Script + ")?" + "(-" + Bcp47Region + ")?" + "(-" + Bcp47Variant + ")*" + "(-" + Bcp47Extension + ")*" + "(-" + Bcp47PrivateUse + ")?" + ")"
  val Bcp47LanguagetagRegExp: Regex =
    ("^(" + Bcp47Grandfathered + "|" + Bcp47Langtag + "|" + Bcp47PrivateUse + ")").r

  val BuiltInTypes = Array[String](
    "TableGroup",
    "Table",
    "Schema",
    "Column",
    "Dialect",
    "Template",
    "Datatype"
  )

  val NameRegExp =
    "^([A-Za-z0-9]|(%[A-F0-9][A-F0-9]))([A-Za-z0-9_]|(%[A-F0-9][A-F0-9]))*$".r

  val Properties = Map(
    "@language" -> languageProperty(PropertyType.Context),
    // Context Properties
    "@base" -> linkProperty(PropertyType.Context),
    // common properties
    "@id" -> linkProperty(PropertyType.Common),
    "notes" -> notesProperty(PropertyType.Common),
    "suppressOutput" -> booleanProperty(PropertyType.Common),
    "null" -> nullProperty(PropertyType.Inherited),
    "separator" -> separatorProperty(PropertyType.Inherited),
    "lang" -> languageProperty(PropertyType.Inherited),
    "default" -> stringProperty(PropertyType.Inherited),
    "required" -> booleanProperty(PropertyType.Inherited),
    "ordered" -> booleanProperty(PropertyType.Inherited),
    "datatype" -> datatypeProperty(PropertyType.Inherited),
    "tableSchema" -> tableSchemaProperty(PropertyType.Table),
    "transformations" -> transformationsProperty(PropertyType.Table),
    "aboutUrl" -> uriTemplateProperty(PropertyType.Inherited),
    "propertyUrl" -> uriTemplateProperty(PropertyType.Inherited),
    "valueUrl" -> uriTemplateProperty(PropertyType.Inherited),
    "textDirection" -> textDirectionProperty(PropertyType.Inherited),
    "dialect" -> dialectProperty(PropertyType.Common),
    // Column level properties
    "titles" -> naturalLanguageProperty(PropertyType.Column),
    "virtual" -> booleanProperty(PropertyType.Column),
    "name" -> nameProperty(PropertyType.Column),
    "url" -> linkProperty(PropertyType.Table),
    // Dialect Properties
    "commentPrefix" -> stringProperty(PropertyType.Dialect),
    "delimiter" -> stringProperty(PropertyType.Dialect),
    "doubleQuote" -> booleanProperty(PropertyType.Dialect),
    "encoding" -> encodingProperty(PropertyType.Dialect),
    "header" -> booleanProperty(PropertyType.Dialect),
    "headerRowCount" -> numericProperty(PropertyType.Dialect),
    "lineTerminators" -> arrayProperty(PropertyType.Dialect),
    "quoteChar" -> stringProperty(PropertyType.Dialect),
    "skipBlankRows" -> booleanProperty(PropertyType.Dialect),
    "skipColumns" -> numericProperty(PropertyType.Dialect),
    "skipInitialSpace" -> booleanProperty(PropertyType.Dialect),
    "skipRows" -> numericProperty(PropertyType.Dialect),
    "trim" -> trimProperty(PropertyType.Dialect),
    // Schema Properties
    "columns" -> columnsProperty(PropertyType.Schema),
    "primaryKey" -> columnReferenceProperty(PropertyType.Schema),
    "foreignKeys" -> foreignKeysProperty(PropertyType.Schema),
    "rowTitles" -> columnReferenceProperty(PropertyType.Schema),
    // Transformation properties
    "targetFormat" -> targetFormatProperty(PropertyType.Transformation),
    "scriptFormat" -> scriptFormatProperty(PropertyType.Transformation),
    "source" -> sourceProperty(PropertyType.Transformation),
    // Foreign Key Properties
    "columnReference" -> columnReferenceProperty(PropertyType.ForeignKey),
    "reference" -> referenceProperty(PropertyType.ForeignKey),
    // foreignKey reference properties
    "resource" -> resourceProperty(PropertyType.ForeignKeyReference),
    "schemaReference" -> schemaReferenceProperty(
      PropertyType.ForeignKeyReference
    )
  )

  def checkProperty(
      property: String,
      value: JsonNode,
      baseUrl: String,
      lang: String
  ): (JsonNode, Array[String], PropertyType.Value) = {
    val propertyRegEx = "^[a-z]+:.*$".r
    val matcher = propertyRegEx.pattern.matcher(property)
    if (Properties.contains(property)) {
      val f = Properties(property)
      f(value, baseUrl, lang)
    } else if (
      matcher.matches() && NameSpaces.values.contains(property.split(":")(0))
    ) {
      val (newValue, warnings) = checkCommonPropertyValue(value, baseUrl, lang)
      (newValue, warnings, PropertyType.Annotation)
    } else {
      // property name must be an absolute URI
      try {
        val scheme = Option(new URI(property).getScheme)
        if (scheme.isEmpty)
          return (
            value,
            Array[String]("invalid_property"),
            PropertyType.Undefined
          )
        val (newValue, warnings) =
          checkCommonPropertyValue(value, baseUrl, lang)
        (newValue, warnings, PropertyType.Annotation)
      } catch {
        case e: Exception => {
          (
            value,
            Array[String](s"invalid_property ${e.getMessage}"),
            PropertyType.Undefined
          )
        }
      }
    }
  }

  def checkCommonPropertyValue(
      value: JsonNode,
      baseUrl: String,
      lang: String
  ): (JsonNode, Array[String]) = {
    value match {
      case o: ObjectNode => {
        val valueCopy = o.deepCopy()
        var warnings = Array[String]()
        val fieldsAndValues = Array.from(valueCopy.fields().asScala)
        for (fieldAndValue <- fieldsAndValues) {
          val p = fieldAndValue.getKey
          var v = fieldAndValue.getValue
          p match {
            case "@context" | "@list" | "@set" =>
              throw new MetadataError(s"$p: common property has $p property")
            case "@type" => processCommonPropertyType(valueCopy, p, v)
            case "@id" => {
              if (!baseUrl.isBlank) {
                val matcher =
                  PropertyChecker.startsWithUnderscore.pattern
                    .matcher(v.asText())
                if (matcher.matches) {
                  throw new MetadataError(
                    s"@id must not start with '_:'  -  ${v.asText()}"
                  )
                }
                try {
                  val newValue = new URL(new URL(baseUrl), v.asText())
                  v = new TextNode(newValue.toString)
                } catch {
                  case e: Exception =>
                    throw new MetadataError(
                      s"common property has invalid @id (${v.asText})"
                    )
                }
              }
            }
            case "@value"    => ProcessCommonPropertyValue(valueCopy)
            case "@language" => ProcessCommonPropertyLanguage(valueCopy, v)
            case _ => {
              if (p(0).equals('@')) {
                throw new MetadataError(
                  s"common property has property other than @id, @type, @value or @language beginning with @ (${p})"
                )
              } else {
                val (newValue, w) = checkCommonPropertyValue(v, baseUrl, lang)
                warnings = Array.concat(warnings, w)
                v = newValue
              }
            }
          }
          valueCopy.set(p, v)
          ()
        }
        (valueCopy, warnings)
      }
      case s: TextNode => {
        lang match {
          case "und" => (value, Array[String]())
          case _ => {
            val objectNodeToReturn = JsonNodeFactory.instance.objectNode()
            objectNodeToReturn.set("@value", value)
            objectNodeToReturn.set("@language", new TextNode(lang))
            (objectNodeToReturn, Array[String]())
          }
        }
      }
      case a: ArrayNode => {
        val values = JsonNodeFactory.instance.arrayNode()
        var warnings = Array[String]()
        val arrayNodeElements = Array.from(a.elements().asScala)
        for (arrayNodeElement <- arrayNodeElements) {
          val (newValue, w) =
            checkCommonPropertyValue(arrayNodeElement, baseUrl, lang)
          warnings = Array.concat(warnings, w)
          values.add(newValue)
        }
        (values, warnings)
      }
      case _ =>
        throw new IllegalArgumentException(
          s"Unexcepted input of type ${value.getClass}"
        )
    }
  }

  private def ProcessCommonPropertyValue(valueCopy: ObjectNode) = {
    if (
      (!valueCopy.path("@type").isMissingNode) && (!valueCopy
        .path("@language")
        .isMissingNode)
    ) {
      throw new MetadataError(
        "common property with @value has both @language and @type"
      )
    }
    var fieldNames = Array.from(valueCopy.fieldNames().asScala)
    fieldNames = fieldNames.filter(!_.contains("@type"))
    fieldNames = fieldNames.filter(!_.contains("@language"))
    if (fieldNames.length > 1) {
      throw new MetadataError(
        "common property with @value has properties other than @language or @type"
      )
    }
  }

  private def ProcessCommonPropertyLanguage(
      valueCopy: ObjectNode,
      v: JsonNode
  ) = {
    if (valueCopy.path("@value").isMissingNode) {
      throw new MetadataError("common property with @language lacks a @value")
    }
    val matcher = Bcp47Language.r.pattern.matcher(v.asText())
    if (!matcher.matches() || v.isEmpty) {
      throw new MetadataError(
        s"common property has invalid @language (${v.asText()})"
      )
    }
  }

  private def processCommonPropertyType(
      valueCopy: ObjectNode,
      p: String,
      v: JsonNode
  ): Unit = {
    val valueNode = valueCopy.path("@value")
    v match {
      case s: TextNode => {
        if (
          (!valueNode.isMissingNode) && BuiltInDataTypes.types
            .contains(s.asText)
        ) {
          // No exceptions raised in this case
        } else if (
          (valueNode.isMissingNode) && BuiltInTypes.contains(
            s.asText
          )
        ) {
          // No exceptions raised in this case as well
        } else {
          val arr: ArrayNode = JsonNodeFactory.instance.arrayNode()
          arr.add(s)
          processCommonPropertyType(valueCopy, p, arr)
        }
      }
      case a: ArrayNode => {
        val typeArrayElements = Array.from(a.elements().asScala)
        val propertyRegEx = "^[a-z]+:.*$".r
        for (typeElement <- typeArrayElements) {
          // typeElement should be Textual here
          val matcher = propertyRegEx.pattern.matcher(typeElement.asText())
          if (
            matcher.matches() && NameSpaces.values.contains(
              typeElement.asText.split(":")(0)
            )
          ) {
            // No exceptions raised in this case
          } else {
            // typeElement Must be an absolute URI
            try {
              val scheme = new URI(typeElement.asText).getScheme
              if (scheme.isEmpty)
                throw new MetadataError(
                  s"common property has invalid @type (${typeElement.asText})"
                )
            } catch {
              case e: Exception =>
                throw new MetadataError(
                  s"common property has invalid @type (${typeElement.asText})"
                )
            }
          }
        }
      }
    }
  }

  def convertValueFacet(
      value: ObjectNode,
      property: String,
      datatype: String
  ): Array[String] = {
    if (!value.path(property).isMissingNode) {
      if (
        PropertyCheckerConstants.DateFormatDataTypes.contains(datatype) ||
        PropertyCheckerConstants.NumericFormatDataTypes.contains(datatype)
      ) {
        return Array[String]()
      } else {
        throw new MetadataError(
          s"$property is only allowed for numeric, date/time and duration types"
        )
      }
    }
    return Array[String]()
  }

  def booleanProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = {
    return (value, baseUrl, lang) => {
      if (value.isBoolean) {
        (value, Array[String](), csvwPropertyType)
      } else {
        (
          BooleanNode.getFalse,
          Array[String](PropertyChecker.invalidValueWarning),
          csvwPropertyType
        )
      }
    }
  }

  def stringProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = {
    return (value, baseUrl, lang) => {
      if (value.isTextual) {
        (value, Array[String](), csvwPropertyType)
      } else {
        (
          new TextNode(""),
          Array[String](PropertyChecker.invalidValueWarning),
          csvwPropertyType
        )
      }
    }
  }

  def numericProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = {
    return (value, baseUrl, lang) => {
      if (value.isInt && value.asInt >= 0) {
        (value, Array[String](), csvwPropertyType)
      } else if (value.isInt && value.asInt < 0) {
        (
          NullNode.getInstance(),
          Array[String](PropertyChecker.invalidValueWarning),
          csvwPropertyType
        )
      } else {
        (
          NullNode.getInstance(),
          Array[String](PropertyChecker.invalidValueWarning),
          csvwPropertyType
        )
      }
    }
  }

  def notesProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = {
    def notesPropertyInternal(
        value: JsonNode,
        baseUrl: String,
        lang: String
    ): (JsonNode, Array[String], PropertyType.Value) = {
      if (value.isArray) {
        val arrayValue = value.asInstanceOf[ArrayNode]
        val elements = Array.from(arrayValue.elements().asScala)

        /**
          * [(1,2), (3,4), (5,6)] => ([1,3,5], [2,4,6])
          */
        val (values, warnings) = Array
          .from(elements.map(x => checkCommonPropertyValue(x, baseUrl, lang)))
          .unzip
        // warnings at this point will be of type Array[Array[String]]
        var newWarnings = Array[String]()
        for (w <- warnings) { newWarnings = Array.concat(newWarnings, w) }
        val arrayNode: ArrayNode = PropertyChecker.mapper.valueToTree(values)
        return (arrayNode, newWarnings, csvwPropertyType)
      }
      (
        JsonNodeFactory.instance.arrayNode(),
        Array[String](PropertyChecker.invalidValueWarning),
        csvwPropertyType
      )
    }
    notesPropertyInternal
  }

  def nullProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      if (value.isTextual) {
        (value, Array[String](), csvwPropertyType)
      } else if (value.isArray) {
        var values = Array[String]()
        var warnings = Array[String]()
        for (x <- value.elements().asScala) {
          x match {
            case xs: TextNode => values = values :+ xs.asText()
            case _            => warnings = warnings :+ PropertyChecker.invalidValueWarning
          }
        }
        val arrayNode: ArrayNode = PropertyChecker.mapper.valueToTree(values)
        (arrayNode, warnings, csvwPropertyType)
      } else {
        val warnings = if (value.isNull) {
          Array[String]()
        } else {
          Array[String](PropertyChecker.invalidValueWarning)
        }
        val arrayNodeToReturn = JsonNodeFactory.instance.arrayNode()
        arrayNodeToReturn.add("")
        (
          arrayNodeToReturn,
          warnings,
          csvwPropertyType
        )
      }
    }
  }

  def separatorProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      value match {
        case s if s.isTextual || s.isNull =>
          (s, Array[String](), csvwPropertyType)
        case _ =>
          (
            NullNode.getInstance(),
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def linkProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (v, baseUrl, lang) =>
    {
      v match {
        case s: TextNode => {
          val matcher =
            PropertyChecker.startsWithUnderscore.pattern.matcher(s.asText())
          if (matcher.matches) {
            throw new MetadataError(s"URL ${s.asText} starts with _:")
          }
          val baseUrlCopy = baseUrl match {
            case "" => s.asText()
            case _  => new URL(new URL(baseUrl), s.asText()).toString
          }
          (new TextNode(baseUrlCopy), Array[String](), csvwPropertyType)
        }
        case _ =>
          (
            new TextNode(""),
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def languageProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      value match {
        case s: TextNode
            if PropertyChecker.Bcp47LanguagetagRegExp.pattern
              .matcher(s.asText())
              .matches =>
          (s, Array[String](), csvwPropertyType)
        case _ =>
          (
            new TextNode(""),
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def datatypeProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      var warnings = Array[String]()
      var valueCopy = value
        .deepCopy()
        .asInstanceOf[JsonNode]
      valueCopy match {
        case datatypeNode: ObjectNode => {
          if (!datatypeNode.path("@id").isMissingNode) {
            val idValue = datatypeNode.get("@id").asText()
            val builtInDataTypeValues = BuiltInDataTypes.types.values.toList
            if (builtInDataTypeValues.contains(idValue)) {
              throw new MetadataError(
                s"datatype @id must not be the id of a built-in datatype ($idValue)"
              )
            } else {
              val (_, w, _) = linkProperty(PropertyType.Common)(
                datatypeNode.get("@id"),
                baseUrl,
                lang
              )
              if (!w.isEmpty) {
                warnings = Array.concat(warnings, w)
                datatypeNode.remove("@id")
              }
            }
          }

          if (!datatypeNode.path("base").isMissingNode) {
            val baseValue = datatypeNode.get("base").asText()
            if (BuiltInDataTypes.types.contains(baseValue)) {
              datatypeNode.put("base", BuiltInDataTypes.types(baseValue))
            } else {
              datatypeNode.put("base", BuiltInDataTypes.types("string"))
              warnings = warnings :+ "invalid_datatype_base"
            }
          } else {
            datatypeNode.put("base", BuiltInDataTypes.types("string"))
          }
        }
        case x: TextNode if (BuiltInDataTypes.types.contains(x.asText())) => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", BuiltInDataTypes.types(x.asText()))
          valueCopy = objectNode
        }
        case x: TextNode => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", BuiltInDataTypes.types("string"))
          valueCopy = objectNode
          warnings = warnings :+ PropertyChecker.invalidValueWarning
        }
      }
      val datatypeNode = valueCopy.asInstanceOf[ObjectNode]
      if (!datatypeNode.path("base").isMissingNode) {
        val baseValue = datatypeNode.get("base").asText()
        if (
          !PropertyCheckerConstants.StringDataTypes.contains(
            baseValue
          ) && !PropertyCheckerConstants.BinaryDataTypes.contains(baseValue)
        ) {
          if (!datatypeNode.path("length").isMissingNode) {
            throw new MetadataError(
              s"datatypes based on $baseValue cannot have a length facet"
            )
          }
          if (!datatypeNode.path("minLength").isMissingNode) {
            throw new MetadataError(
              s"datatypes based on $baseValue cannot have a minLength facet"
            )
          }
          if (!datatypeNode.path("maxLength").isMissingNode) {
            throw new MetadataError(
              s"datatypes based on $baseValue cannot have a maxLength facet"
            )
          }
        }
      }

      if (!datatypeNode.path("minimum").isMissingNode) {
        datatypeNode.put("minInclusive", valueCopy.get("minimum").asText())
        datatypeNode.remove("minimum")
      }

      if (!datatypeNode.path("maximum").isMissingNode) {
        datatypeNode.put("maxInclusive", valueCopy.get("maximum").asText())
        datatypeNode.remove("maximum")
      }

      val baseValue = if (datatypeNode.path("base").isMissingNode) {
        ""
      } else {
        datatypeNode.get("base").asText
      }

      warnings :+ convertValueFacet(datatypeNode, "minInclusive", baseValue)
      warnings :+ convertValueFacet(datatypeNode, "minExclusive", baseValue)
      warnings :+ convertValueFacet(datatypeNode, "maxInclusive", baseValue)
      warnings :+ convertValueFacet(datatypeNode, "maxExclusive", baseValue)

      val minInclusive: Option[String] =
        getRangeConstraint(datatypeNode, "minInclusive")
      val maxInclusive: Option[String] =
        getRangeConstraint(datatypeNode, "maxInclusive")
      val minExclusive: Option[String] =
        getRangeConstraint(datatypeNode, "minExclusive")
      val maxExclusive: Option[String] =
        getRangeConstraint(datatypeNode, "maxExclusive")

      (minInclusive, minExclusive, maxInclusive, maxExclusive) match {
        case (Some(minI), Some(minE), _, _) =>
          throw MetadataError(
            s"datatype cannot specify both minimum/minInclusive (${minI}) and minExclusive (${minE})"
          )
        case (_, _, Some(maxI), Some(maxE)) =>
          throw MetadataError(
            s"datatype cannot specify both maximum/maxInclusive (${maxI}) and maxExclusive (${maxE})"
          )
        case _ => {}
      }

      if (PropertyCheckerConstants.NumericFormatDataTypes.contains(baseValue)) {
        (
          minInclusive.map(BigDecimal(_)),
          minExclusive.map(BigDecimal(_)),
          maxInclusive.map(BigDecimal(_)),
          maxExclusive.map(BigDecimal(_))
        ) match {
          case (Some(minI), _, Some(maxI), _) if minI > maxI =>
            throw MetadataError(
              s"datatype minInclusive ($minI) cannot be greater than maxInclusive ($maxI)"
            )
          case (Some(minI), _, _, Some(maxE)) if minI >= maxE =>
            throw MetadataError(
              s"datatype minInclusive ($minI) cannot be greater than or equal to maxExclusive ($maxE)"
            )
          case (_, Some(minE), _, Some(maxE)) if minE > maxE =>
            throw MetadataError(
              s"datatype minExclusive ($minE) cannot be greater than or equal to maxExclusive ($maxE)"
            )
          case (_, Some(minE), Some(maxI), _) if minE >= maxI =>
            throw MetadataError(
              s"datatype minExclusive ($minE) cannot be greater than maxInclusive ($maxI)"
            )
          case _ => {}
        }
      } else if (
        PropertyCheckerConstants.DateFormatDataTypes.contains(baseValue)
      ) {
        (
          minInclusive.map(DateTime.parse),
          minExclusive.map(DateTime.parse),
          maxInclusive.map(DateTime.parse),
          maxExclusive.map(DateTime.parse)
        ) match {
          case (Some(minI), _, Some(maxI), _)
              if minI.getMillis > maxI.getMillis =>
            throw MetadataError(
              s"datatype minInclusive ($minI) cannot be greater than maxInclusive ($maxI)"
            )
          case (Some(minI), _, _, Some(maxE))
              if minI.getMillis >= maxE.getMillis =>
            throw MetadataError(
              s"datatype minInclusive ($minI) cannot be greater than or equal to maxExclusive ($maxE)"
            )
          case (_, Some(minE), _, Some(maxE))
              if minE.getMillis > maxE.getMillis =>
            throw MetadataError(
              s"datatype minExclusive ($minE) cannot be greater than or equal to maxExclusive ($maxE)"
            )
          case (_, Some(minE), Some(maxI), _)
              if minE.getMillis >= maxI.getMillis =>
            throw MetadataError(
              s"datatype minExclusive ($minE) cannot be greater than maxInclusive ($maxI)"
            )
          case _ => {}
        }
      }

      val minLength = datatypeNode.path("minLength")
      val maxLength = datatypeNode.path("maxLength")
      val length = datatypeNode.path("length")
      if (
        !length.isMissingNode && !minLength.isMissingNode && length.asInt < minLength.asInt
      ) {
        throw new MetadataError(
          s"datatype length ($length) cannot be less than minLength ($minLength)"
        )
      }
      if (
        !length.isMissingNode && !maxLength.isMissingNode && length.asInt > maxLength.asInt
      ) {
        throw new MetadataError(
          s"datatype length ($length) cannot be more than maxLength ($maxLength)"
        )
      }
      if (
        !minLength.isMissingNode && !maxLength.isMissingNode && minLength.asInt > maxLength.asInt
      ) {
        throw new MetadataError(
          s"datatype minLength ($minLength) cannot be more than maxLength ($maxLength)"
        )
      }

      if (!datatypeNode.path("format").isMissingNode) {
        val baseValue = datatypeNode.get("base").asText()
        if (
          PropertyCheckerConstants.RegExpFormatDataTypes.contains(baseValue)
        ) {
          try {
            // In ruby regexp is stored in format key. Also regexp validated. Determine how to handle this in scala
            // value["format"] = Regexp.new(value["format"])
            datatypeNode
              .get("format")
              .asText
              .r
          } catch {
            case e: Exception => {
              datatypeNode.remove("format")
              warnings = warnings :+ s"invalid_regex - ${e.getMessage}"
            }
          }
        } else if (
          PropertyCheckerConstants.NumericFormatDataTypes.contains(baseValue)
        ) {
          warnings = warnings.concat(
            processNumericDatatypeAndReturnWarnings(datatypeNode)
          )
        } else if (baseValue == "http://www.w3.org/2001/XMLSchema#boolean") {
          if (datatypeNode.get("format").isTextual) {
            val formatValues = datatypeNode.get("format").asText.split("""\|""")
            if (formatValues.length != 2) {
              datatypeNode.remove("format")
              warnings = warnings :+ "invalid_boolean_format"
            }
          } else {
            // Boolean formats should always be textual
            datatypeNode.remove("format")
            warnings = warnings :+ "invalid_boolean_format"
          }
        } else if (
          PropertyCheckerConstants.DateFormatDataTypes.contains(baseValue)
        ) {
          if (datatypeNode.get("format").isTextual) {
            try {
              val dateFormatString = datatypeNode.get("format").asText()
              val format =
                DateFormat(Some(dateFormatString), baseValue).getFormat()
              if (format.isDefined) {
                datatypeNode.set("format", new TextNode(format.get))
              }
            } catch {
              case e: DateFormatError => {
                datatypeNode.remove("format")
                warnings = warnings :+ "invalid_date_format"
              }
            }
          } else {
            datatypeNode.remove("format")
            warnings = warnings :+ "invalid_date_format"
          }
        }
      }
      (datatypeNode, warnings, csvwPropertyType)
    }
  }

  private def getRangeConstraint(
      datatypeNode: ObjectNode,
      property: String
  ) = {
    datatypeNode
      .getMaybeNode(property)
      .flatMap {
        case rangeConstraint: ObjectNode =>
          rangeConstraint
            .getMaybeNode("dateTime")
            .map(rangeConstraint => rangeConstraint.asText())
        case rangeConstraint: TextNode =>
          Some(rangeConstraint.asText())
        case rangeConstraint: IntNode =>
          Some(rangeConstraint.asText())
        case rangeConstraint: DecimalNode =>
          Some(rangeConstraint.asText())
        case rangeConstraint: LongNode =>
          Some(rangeConstraint.asText())
      }
  }

  def processNumericDatatypeAndReturnWarnings(
      objectNode: ObjectNode
  ): Array[String] = {
    var format = objectNode.get("format")
    var warnings = Array[String]()
    if (format.isTextual) {
      val patternObj = JsonNodeFactory.instance.objectNode()
      patternObj.set("pattern", format)
      objectNode.set("format", patternObj)
    }
    try {
      format = objectNode.get("format")
      val groupChar = if (format.path("groupChar").isMissingNode) {
        None
      } else {
        Some(format.get("groupChar").asText.charAt(0))
      }
      val decimalChar = if (format.path("decimalChar").isMissingNode) {
        None
      } else {
        Some(format.get("decimalChar").asText.charAt(0))
      }
      val patternNode = format.path("pattern")
      val patternString: Option[String] =
        if (patternNode.isMissingNode) None else Some(patternNode.asText())

      patternString
        .map(pattern =>
          LdmlNumberFormatParser(
            groupChar.getOrElse(','),
            decimalChar.getOrElse('.')
          ).getParserForFormat(pattern)
        )
    } catch {
      case e: NumberFormatError => {
        format.asInstanceOf[ObjectNode].remove("pattern")
        warnings = warnings :+ e.getMessage :+ "invalid_number_format"
      }
    }
    warnings
  }

  def tableSchemaProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = {
    def tableSchemaPropertyInternal(
        value: JsonNode,
        baseUrl: String,
        lang: String
    ): (JsonNode, Array[String], PropertyType.Value) = {
      var schemaBaseUrl = new URL(baseUrl)
      var schemaLang = lang

      var schemaJson: ObjectNode = null
      if (value.isTextual) {
        val schemaUrl = new URL(new URL(baseUrl), value.asText())
        schemaJson =
          PropertyChecker.mapper.readTree(schemaUrl).asInstanceOf[ObjectNode]
        if (!schemaJson.path("@id").isMissingNode) {
          // Do something here as object node put method doesn't allow uri object
          val absoluteSchemaUrl =
            new URL(schemaUrl, schemaJson.get("@id").asText())
          schemaJson.put("@id", absoluteSchemaUrl.toString)
        } else {
          schemaJson.put("@id", schemaUrl.toString)
        }
        val (newSchemaBaseUrl, newSchemaLang) =
          fetchSchemaBaseUrlAndLangAndRemoveContext(
            schemaJson,
            schemaUrl,
            schemaLang
          )
        schemaBaseUrl = newSchemaBaseUrl
        schemaLang = newSchemaLang
      } else if (value.isObject) {
        schemaJson = value.deepCopy()
      } else {
        return (
          NullNode.getInstance(),
          Array[String](PropertyChecker.invalidValueWarning),
          PropertyType.Table
        )
      }

      var warnings = Array[String]()
      val fieldsAndValues = Array.from(schemaJson.fields.asScala)
      for (fieldAndValue <- fieldsAndValues) {
        warnings ++= validateObjectAndUpdateSchemaJson(
          schemaJson,
          schemaBaseUrl,
          schemaLang,
          fieldAndValue.getKey,
          fieldAndValue.getValue
        )
      }
      return (schemaJson, warnings, PropertyType.Table)
    }
    return tableSchemaPropertyInternal
  }

  def validateObjectAndUpdateSchemaJson(
      schemaJson: ObjectNode,
      schemaBaseUrl: URL,
      schemaLang: String,
      property: String,
      value: JsonNode
  ): Array[String] = {
    var warnings = Array[String]()
    if (property == "@id") {
      val matcher =
        PropertyChecker.startsWithUnderscore.pattern.matcher(value.asText())
      if (matcher.matches) {
        throw new MetadataError(s"@id ${value.asText} starts with _:")
      }
    } else if (property == "@type") {
      if (value.asText() != "Schema") {
        throw new MetadataError("@type of schema is not 'Schema'")
      }
    } else {
      val (validatedV, warningsForP, propertyType) =
        checkProperty(property, value, schemaBaseUrl.toString, schemaLang)
      warnings ++= warningsForP
      if (
        (propertyType == PropertyType.Schema || propertyType == PropertyType.Inherited) && warningsForP.isEmpty
      ) {
        schemaJson.set(property, validatedV)
      } else {
        schemaJson.remove(property)
        if (
          propertyType != PropertyType.Schema && propertyType != PropertyType.Inherited
        ) {
          warnings :+= "invalid_property"
        }
      }
    }
    warnings
  }

  def fetchSchemaBaseUrlAndLangAndRemoveContext(
      schemaJson: ObjectNode,
      schemaBaseUrl: URL,
      schemaLang: String
  ): (URL, String) = {
    if (!schemaJson.path("@context").isMissingNode) {
      if (schemaJson.isArray && schemaJson.size > 1) {
        val secondContextElement =
          Array.from(schemaJson.get("@context").elements.asScala).apply(1)
        val maybeBaseNode = secondContextElement.path("@base")
        val newSchemaBaseUrl = if (!maybeBaseNode.isMissingNode) {
          new URL(schemaBaseUrl, maybeBaseNode.asText())
        } else {
          schemaBaseUrl
        }
        val languageNode = secondContextElement.path("@language")
        val newSchemaLang = if (!languageNode.isMissingNode) {
          languageNode.asText()
        } else {
          schemaLang
        }
        schemaJson.remove("@context")
        return (newSchemaBaseUrl, newSchemaLang)
      }
      schemaJson.remove("@context")
    }
    (schemaBaseUrl, schemaLang)
  }

  def foreignKeysProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      var foreignKeys = Array[JsonNode]()
      var warnings = Array[String]()
      value match {
        case xs: ArrayNode => {
          val arrayNodes = Array.from(xs.elements().asScala)
          for (foreignKey <- arrayNodes) {
            val (fk, warn) = foreignKeyCheckIfValid(foreignKey, baseUrl, lang)
            foreignKeys = foreignKeys :+ fk
            warnings = Array.concat(warnings, warn)
          }
        }
        case _ => warnings = warnings :+ PropertyChecker.invalidValueWarning
      }
      val arrayNode: ArrayNode = PropertyChecker.mapper.valueToTree(foreignKeys)
      (arrayNode, warnings, PropertyType.Schema)
    }
  }

  def foreignKeyCheckIfValid(
      foreignKey: JsonNode,
      baseUrl: String,
      lang: String
  ): (JsonNode, Array[String]) = {
    var warnings = Array[String]()
    foreignKey match {
      case o: ObjectNode => {
        val foreignKeyCopy = foreignKey
          .deepCopy()
          .asInstanceOf[ObjectNode]
        val foreignKeysElements = Array.from(foreignKeyCopy.fields().asScala)
        for (f <- foreignKeysElements) {
          val p = f.getKey
          val matcher = PropertyChecker.containsColon.pattern.matcher(p)
          if (matcher.matches()) {
            throw new MetadataError(
              "foreignKey includes a prefixed (common) property"
            )
          }
          val (value, w, typeString) =
            checkProperty(p, f.getValue, baseUrl, lang)
          if (typeString == PropertyType.ForeignKey && w.isEmpty) {
            foreignKeyCopy.set(p, value)
          } else {
            foreignKeyCopy.remove(p)
            warnings = warnings :+ PropertyChecker.invalidValueWarning
            warnings = Array.concat(warnings, w)
          }
        }
        (foreignKeyCopy, warnings)
      }
      case _ => {
        val foreignKeyCopy = JsonNodeFactory.instance.objectNode()
        warnings = warnings :+ "invalid_foreign_key"
        (foreignKeyCopy, warnings)
      }
    }
  }

  def referenceProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      value match {
        case obj: ObjectNode => {
          val valueCopy = obj.deepCopy()
          var warnings = Array[String]()
          val valueCopyElements = Array.from(valueCopy.fields().asScala)
          for (e <- valueCopyElements) {
            val p = e.getKey
            val v = e.getValue
            val matcher = PropertyChecker.containsColon.pattern.matcher(p)
            // Check if property is included in the valid properties for a foreign key object
            if (
              Array[String]("resource", "schemaReference", "columnReference")
                .contains(p)
            ) {
              val (newValue, warning, propertyType) =
                checkProperty(p, v, baseUrl, lang)
              if (warning.isEmpty) {
                valueCopy.set(p, newValue)
              } else {
                valueCopy.remove(p)
                warnings = Array.concat(warnings, warning)
              }
            } else if (matcher.matches()) {
              throw new MetadataError(
                s"foreignKey reference ($p) includes a prefixed (common) property"
              )
            } else {
              valueCopy.remove(p)
              warnings = warnings :+ PropertyChecker.invalidValueWarning
            }
          }
          if (valueCopy.path("columnReference").isMissingNode) {
            throw new MetadataError(
              "foreignKey reference columnReference is missing"
            )
          }
          if (
            valueCopy
              .path("resource")
              .isMissingNode && valueCopy.path("schemaReference").isMissingNode
          ) {
            throw new MetadataError(
              "foreignKey reference does not have either resource or schemaReference"
            ) // Should have at least one of them, else it is an error
          }
          if (
            !valueCopy
              .path("resource")
              .isMissingNode && !valueCopy.path("schemaReference").isMissingNode
          ) {
            throw new MetadataError(
              "foreignKey reference has both resource and schemaReference"
            )
          }
          (valueCopy, warnings, csvwPropertyType)
        }
        case _ =>
          throw new MetadataError("foreignKey reference is not an object")
      }
    }
  }

  def uriTemplateProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      value match {
        // Don't know how to place a URI object in JsonNode, keeping the text value as of now
        case s: TextNode => (s, Array[String](), csvwPropertyType)
        case _ =>
          (
            new TextNode(""),
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def textDirectionProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      value match {
        case s: TextNode
            if Array[String]("ltr", "rtl", "inherit").contains(s.asText()) => {
          (value, Array[String](), PropertyType.Inherited)
        }
        case _ =>
          (
            new TextNode(PropertyType.Inherited.toString),
            Array[String](PropertyChecker.invalidValueWarning),
            PropertyType.Inherited
          )
      }
    }
  }

  def naturalLanguageProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      var warnings = Array[String]()
      value match {
        case s: TextNode => {
          val returnObject = JsonNodeFactory.instance.objectNode()
          val arrayNode = JsonNodeFactory.instance.arrayNode()
          arrayNode.add(s.asText())
          returnObject.set(lang, arrayNode)
          (returnObject, Array[String](), csvwPropertyType)
        }
        case a: ArrayNode => {
          var validTitles = Array[String]()
          val arrayNodeElements = Array.from(a.elements().asScala)
          for (element <- arrayNodeElements) {
            element match {
              case s: TextNode => validTitles = validTitles :+ s.asText()
              case _ => {
                warnings =
                  warnings :+ a.toPrettyString + " is invalid, textual elements expected"
                warnings = warnings :+ PropertyChecker.invalidValueWarning
              }
            }
          }
          val returnObject = JsonNodeFactory.instance.objectNode()
          val arrayNode: ArrayNode =
            PropertyChecker.mapper.valueToTree(validTitles)
          returnObject.set(lang, arrayNode)
          (returnObject, warnings, csvwPropertyType)
        }
        case o: ObjectNode => {
          val (valueCopy, w) = processNaturalLanguagePropertyObject(o)
          warnings = Array.concat(warnings, w)
          (valueCopy, warnings, csvwPropertyType)
        }
        case _ =>
          (
            NullNode.getInstance(),
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def processNaturalLanguagePropertyObject(
      value: ObjectNode
  ): (ObjectNode, Array[String]) = {
    val valueCopy = value.deepCopy()
    var warnings = Array[String]()
    val fieldsAndValues = Array.from(valueCopy.fields.asScala)
    for (fieldAndValue <- fieldsAndValues) {
      val elementKey = fieldAndValue.getKey
      val matcher =
        PropertyChecker.Bcp47LanguagetagRegExp.pattern.matcher(elementKey)
      if (matcher.matches()) {
        var validTitles = Array[String]()
        fieldAndValue.getValue match {
          case s: TextNode => validTitles = validTitles :+ s.asText()
          case a: ArrayNode => {
            val titles = Array.from(a.elements().asScala)
            for (title <- titles) {
              title match {
                case s: TextNode => validTitles = validTitles :+ s.asText()
                case _ => {
                  warnings =
                    warnings :+ a.toPrettyString + " is invalid, textual elements expected"
                  warnings = warnings :+ PropertyChecker.invalidValueWarning
                }
              }
            }
          }
          case _ => {
            warnings =
              warnings :+ fieldAndValue.getValue.toPrettyString + " is invalid, array or textual elements expected"
            warnings = warnings :+ PropertyChecker.invalidValueWarning
          }
        }
        val validTitlesArrayNode: ArrayNode =
          PropertyChecker.mapper.valueToTree(validTitles)
        valueCopy.set(elementKey, validTitlesArrayNode)
      } else {
        valueCopy.remove(elementKey)
        warnings = warnings :+ "invalid_language"
      }
    }
    (valueCopy, warnings)
  }

  def nameProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      value match {
        case s: TextNode => {
          val matcher = PropertyChecker.NameRegExp.pattern.matcher(s.asText())
          if (matcher.matches()) {
            (s, Array[String](), PropertyType.Column)
          } else {
            (
              NullNode.instance,
              Array[String](PropertyChecker.invalidValueWarning),
              csvwPropertyType
            )
          }
        }
        case _ =>
          (
            NullNode.instance,
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def encodingProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      value match {
        case s: TextNode
            if PropertyCheckerConstants.ValidEncodings.contains(s.asText()) => {
          (s, Array[String](), csvwPropertyType)
        }
        case _ =>
          (
            NullNode.instance,
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def arrayProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      value match {
        case a: ArrayNode => (a, Array[String](), csvwPropertyType)
        case _ =>
          (
            BooleanNode.getFalse,
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def trimProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      var valueCopy: JsonNode = value.deepCopy()
      valueCopy match {
        case b: BooleanNode => {
          if (b.booleanValue) {
            valueCopy = new TextNode("true")
          } else {
            valueCopy = new TextNode("false")
          }
        }
        case _ => {}
      }
      if (
        Array[String]("true", "false", "start", "end")
          .contains(valueCopy.asText())
      ) {
        (valueCopy, Array[String](), csvwPropertyType)
      } else {
        (
          new TextNode("false"),
          Array[String](PropertyChecker.invalidValueWarning),
          csvwPropertyType
        )
      }
    }
  }

  def columnsProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      (value, Array[String](), csvwPropertyType)
    }
  }

  def columnReferenceProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      value match {
        case s: TextNode => {
          val arrayNode = JsonNodeFactory.instance.arrayNode()
          arrayNode.add(s)
          (arrayNode, Array[String](), csvwPropertyType)
        }
        case a: ArrayNode => (a, Array[String](), csvwPropertyType)
      }
    }
  }

  def targetFormatProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      (value, Array[String](), csvwPropertyType)
    }
  }

  def scriptFormatProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      (value, Array[String](), csvwPropertyType)
    }
  }

  def sourceProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      (value, Array[String](), csvwPropertyType)
    }
  }

  def resourceProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, _, _) =>
    {
      (value, Array[String](), csvwPropertyType)
    }
  }

  def schemaReferenceProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, _) =>
    {
      val url = new URL(new URL(baseUrl), value.asText()).toString
      // Don't know how to place a URI object in JsonNode, keeping the text value as of now
      (new TextNode(url), Array[String](), csvwPropertyType)
    }
  }

  def dialectProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      value match {
        case o: ObjectNode => {
          val valueCopy = o.deepCopy()
          var warnings = Array[String]()
          val fieldsAndValues = Array.from(valueCopy.fields.asScala)
          for (fieldAndValue <- fieldsAndValues) {
            val key = fieldAndValue.getKey
            val v = fieldAndValue.getValue
            key match {
              case "@id" => {
                val matcher = PropertyChecker.startsWithUnderscore.pattern
                  .matcher(v.asText())
                if (matcher.matches) {
                  throw new MetadataError("@id starts with _:")
                }
              }
              case "@type" => {
                if (v.asText() != "Dialect") {
                  throw new MetadataError("@type of dialect is not 'Dialect'")
                }
              }
              case _ => {
                val (newValue, w, t) = checkProperty(key, v, baseUrl, lang)
                if (t == PropertyType.Dialect && w.isEmpty) {
                  valueCopy.set(key, newValue)
                } else {
                  valueCopy.remove(key)
                  if (t != PropertyType.Dialect) {
                    warnings = warnings :+ "invalid_property"
                  }
                  warnings = Array.concat(warnings, w)
                }
              }
            }
          }
          (valueCopy, warnings, csvwPropertyType)
        }
        case _ =>
          // May be we might need to support dialect property of type other than ObjectNode.
          //  The dialect of a table is an object property. It could be provided as a URL that indicates
          //  a commonly used dialect, like this:
          //  "dialect": "http://example.org/tab-separated-values"
          (
            NullNode.instance,
            Array[String](PropertyChecker.invalidValueWarning),
            csvwPropertyType
          )
      }
    }
  }

  def transformationsProperty(
      csvwPropertyType: PropertyType.Value
  ): (JsonNode, String, String) => (
      JsonNode,
      Array[String],
      PropertyType.Value
  ) = { (value, baseUrl, lang) =>
    {
      var warnings = Array[String]()
      val transformationsToReturn = JsonNodeFactory.instance.arrayNode()
      value match {
        case a: ArrayNode => {
          val transformationsArr = Array.from(a.elements().asScala)
          for ((transformation, index) <- transformationsArr.zipWithIndex) {
            transformation match {
              case o: ObjectNode => {
                val w = processTransformationObjectAndReturnWarnings(
                  transformationsToReturn,
                  o,
                  index,
                  baseUrl,
                  lang
                )
                warnings = Array.concat(warnings, w)
              }
              case _ => warnings = warnings :+ "invalid_transformation"
            }
          }
        }
        case _ => warnings = warnings :+ PropertyChecker.invalidValueWarning
      }
      (transformationsToReturn, warnings, csvwPropertyType)
    }
  }

  def processTransformationObjectAndReturnWarnings(
      transformationsToReturn: ArrayNode,
      transformationsMainObject: ObjectNode,
      index: Int,
      baseUrl: String,
      lang: String
  ): Array[String] = {
    val transformationObjects =
      Array.from(transformationsMainObject.fields().asScala)
    var warnings = Array[String]()
    for (elem <- transformationObjects) {
      val property = elem.getKey
      val value = elem.getValue
      property match {
        case "@id" => {
          val matcher =
            PropertyChecker.startsWithUnderscore.pattern.matcher(value.asText())
          if (matcher.matches) {
            throw new MetadataError(
              s"transformations[$index].@id starts with _:"
            )
          }
        }
        case "@type" if value.asText() != "Template" => {
          throw new MetadataError(
            s"transformations[$index].@type  @type of transformation is not 'Template'"
          )
        }
        case "url"    => {}
        case "titles" => {}
        case _ => {
          val (_, w, newType) = checkProperty(property, value, baseUrl, lang)
          if (newType != PropertyType.Transformation || !w.isEmpty) {
            transformationsMainObject.remove(property)
            if (newType != PropertyType.Transformation)
              warnings = warnings :+ "invalid_property"
            warnings = Array.concat(warnings, w)
          }
        }
      }
    }
    transformationsToReturn.add(transformationsMainObject)
    warnings
  }
}
