package CSVValidation
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node._

import scala.collection.mutable.HashMap
import scala.jdk.CollectionConverters._
import scala.util.matching.Regex


object PropertyChecker {
  val startsWithUnderscore = "^_:.*".r
  val containsColon = ".*:.*".r
  val mapper = new ObjectMapper
  val invalidValueWarning = "invalid_value"
  val Bcp47Regular = "(art-lojban|cel-gaulish|no-bok|no-nyn|zh-guoyu|zh-hakka|zh-min|zh-min-nan|zh-xiang)"
  val Bcp47Irregular = "(en-GB-oed|i-ami|i-bnn|i-default|i-enochian|i-hak|i-klingon|i-lux|i-mingo|i-navajo|i-pwn|i-tao|i-tay|i-tsu|sgn-BE-FR|sgn-BE-NL|sgn-CH-DE)"
  val Bcp47Grandfathered = "(?<grandfathered>" + Bcp47Irregular + "|" + Bcp47Regular + ")"
  val Bcp47PrivateUse = "(x(-[A-Za-z0-9]{1,8})+)"
  val Bcp47Singleton = "[0-9A-WY-Za-wy-z]"
  val Bcp47Extension = "(?<extension>" + Bcp47Singleton + "(-[A-Za-z0-9]{2,8})+)"
  val Bcp47Variant = "(?<variant>[A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3})"
  val Bcp47Region = "(?<region>[A-Za-z]{2}|[0-9]{3})"
  val Bcp47Script = "(?<script>[A-Za-z]{4})"
  val Bcp47Extlang = "(?<extlang>[A-Za-z]{3}(-[A-Za-z]{3}){0,2})"
  val Bcp47Language = "(?<language>([A-Za-z]{2,3}(-" + Bcp47Extlang + ")?)|[A-Za-z]{4}|[A-Za-z]{5,8})"
  val Bcp47Langtag = "(" + Bcp47Language + "(-" + Bcp47Script + ")?" + "(-" + Bcp47Region + ")?" + "(-" + Bcp47Variant + ")*" + "(-" + Bcp47Extension + ")*" + "(-" + Bcp47PrivateUse + ")?" + ")"
  val Bcp47LanguagetagRegExp: Regex = ("^(" + Bcp47Grandfathered + "|" + Bcp47Langtag + "|" + Bcp47PrivateUse + ")").r

  val BuiltInTypes = Array[String]("TableGroup", "Table", "Schema", "Column", "Dialect", "Template", "Datatype")

  val NameRegExp = "^([A-Za-z0-9]|(%[A-F0-9][A-F0-9]))([A-Za-z0-9_]|(%[A-F0-9][A-F0-9]))*$".r

  val Properties = Map(
    "@language" -> languageProperty (PropertyType.Context),
       // Context Properties
       "@base" -> linkProperty(PropertyType.Context),
      //  Common properties
       "@id" -> linkProperty(PropertyType.Common),
       "notes" -> notesProperty(PropertyType.Common),
       "suppressOutput" -> booleanProperty(PropertyType.Common),
       "null" -> nullProperty(PropertyType.Inherited),
       "separator" -> separatorProperty(PropertyType.Inherited),
       "lang" -> languageProperty(PropertyType.Inherited),
       "default" -> stringProperty(PropertyType.Inherited),
       "commentPrefix" -> stringProperty(PropertyType.Dialect),
       "delimiter" -> stringProperty(PropertyType.Dialect),
       "quoteChar" -> stringProperty(PropertyType.Dialect),
       "headerRowCount" -> numericProperty(PropertyType.Dialect),
       "skipColumns" -> numericProperty(PropertyType.Dialect),
       "skipRows" -> numericProperty(PropertyType.Dialect),
       "datatype" -> datatypeProperty(PropertyType.Inherited)
     )

  def checkProperty(property: String, value: JsonNode, baseUrl:String, lang:String): (JsonNode, Array[String], PropertyType.Value) = {
    // More conditions and logic to add here.
    val f = Properties(property)
    return f(value, baseUrl, lang)
  }

  def checkCommonPropertyValue(value: JsonNode, baseUrl: String, lang: String):(Any, String) = {
    if(value.isObject) {
      throw new NotImplementedError("to be implemented later")
    }
    else if(value.isTextual) {
      val s = value.asText()
      lang match {
        case "und" => return (s, "")
        case _ => {
          val h = HashMap(
            "@value" -> s, "@language" -> lang
          )
          return (h, "")
        }
      }
    } else if(value.isArray) {
      throw new NotImplementedError("To be implemented later") // Check if this have to be an array of strings
    } else {
      throw new IllegalArgumentException(s"Unexcepted input of type ${value.getClass}")
    }
  }

  def convertValueFacet(value:ObjectNode, property:String, datatype:String):Array[String] = {
    if(!value.path(property).isMissingNode) {
      if(PropertyCheckerConstants.DateFormatDataTypes.contains(datatype)) {
        throw new NotImplementedError("To be implemented after implementing DateFormat class")
      } else if (PropertyCheckerConstants.NumericFormatDataTypes.contains(datatype)) {
        return Array[String]()
      } else {
        throw new MetadataError(s"$property is only allowed for numeric, date/time and duration types")
      }
    }
    return Array[String]()
  }

  def booleanProperty(csvwPropertyType:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
    return (value, baseUrl, lang) => {
      if(value.isBoolean) {
        (value, Array[String](), csvwPropertyType)
      } else {
        (BooleanNode.getFalse, Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def stringProperty(csvwPropertyType: PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
    return (value, baseUrl, lang) => {
      if (value.isTextual) {
         (value, Array[String](), csvwPropertyType)
      } else {
         (new TextNode(""), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def numericProperty(csvwPropertyType: PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     return (value, baseUrl, lang) => {
      if (value.isInt && value.asInt >= 0) {
         (value, Array[String](), csvwPropertyType)
      } else if (value.isInt && value.asInt < 0) {
         (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      } else {
         (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def notesProperty(csvwPropertyType:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     def notesPropertyInternal (value: JsonNode, baseUrl: String, lang: String): (JsonNode, Array[String], PropertyType.Value) = {
      if (value.isArray) {
        val arrayValue = value.asInstanceOf[ArrayNode]
        val elements = Array.from(arrayValue.elements().asScala)
        if (elements forall (_.isTextual())) {
          /**
           * [(1,2), (3,4), (5,6)] => ([1,3,5], [2,4,6])
           */
          val (values, warnings) = Array.from(elements.map(x => checkCommonPropertyValue(x, baseUrl, lang))).unzip
          val arrayNode:ArrayNode = PropertyChecker.mapper.valueToTree(values)
          return (arrayNode, warnings, csvwPropertyType)
        }
      }
       (JsonNodeFactory.instance.arrayNode(), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
    }
    return notesPropertyInternal
  }

  def nullProperty(csvwPropertyType:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
      if (value.isTextual) {
         (value, Array[String](), csvwPropertyType)
      } else if (value.isArray) {
        var values = Array[String]()
        var warnings = Array[String]()
        for (x <- value.elements().asScala) {
          x match {
            case xs: TextNode => values = values :+ xs.asText()
            case _ => warnings = warnings :+ PropertyChecker.invalidValueWarning
          }
        }
        val arrayNode:ArrayNode = PropertyChecker.mapper.valueToTree(values)
         (arrayNode, warnings, csvwPropertyType)
      } else {
         (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def separatorProperty(csvwPropertyType:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
       value match {
        case s if s.isTextual || s.isNull => (s, null, csvwPropertyType)
        case _ => (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def linkProperty(csvwPropertyType:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (v, baseUrl, lang) => {
      var baseUrlCopy = ""
      v match {
        case s: TextNode => {
          val matcher = PropertyChecker.startsWithUnderscore.pattern.matcher(s.asText())
          if (matcher.matches) {
            throw new MetadataError(s"URL ${s.asText} starts with _:")
          }
          baseUrlCopy = baseUrl match {
            case "" => s.asText()
            case _ => baseUrl + s.asText()
          }
           (new TextNode(baseUrlCopy), Array[String](""), csvwPropertyType)
        }
        case _ =>  (new TextNode(""), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def languageProperty (csvwPropertyType:PropertyType.Value) : (JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
      value match {
        case s:TextNode if PropertyChecker.Bcp47LanguagetagRegExp.pattern.matcher(s.asText()).matches => (s, Array[String](), csvwPropertyType)
        case _ => (new TextNode(""), Array[String](PropertyChecker.invalidValueWarning), csvwPropertyType)
      }
    }
  }

  def datatypeProperty(csvwPropertyType: PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
      var warnings = Array[String]()
      var valueCopy = value.deepCopy()
        .asInstanceOf[JsonNode]
      valueCopy match {
        case v: ObjectNode => {
          val objectNode = v.asInstanceOf[ObjectNode]
          if (!objectNode.path("@id").isMissingNode) {
            val idValue = objectNode.get("@id").asText()
            if (BuiltInDataTypes.types.contains(idValue)) {
              throw new MetadataError(s"datatype @id must not be the id of a built-in datatype ($idValue)")
            } else {
              val (_, w, _) = linkProperty(PropertyType.Common)(objectNode.get("@id"), baseUrl, lang)
              if (!w.isEmpty) {
                warnings = Array.concat(warnings, w)
                objectNode.remove("@id")
              }
            }
          }

          if (!objectNode.path("base").isMissingNode) {
            val baseValue = objectNode.get("base").asText()
            if (BuiltInDataTypes.types.contains(baseValue)) {
              objectNode.put("base", BuiltInDataTypes.types(baseValue))
            } else {
              objectNode.put("base", BuiltInDataTypes.types("string"))
              warnings = warnings :+ "invalid_datatype_base"
            }
          } else {
            objectNode.put("base", BuiltInDataTypes.types("string"))
          }
        }
        case x: TextNode if (BuiltInDataTypes.types.contains(x.asText())) => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", BuiltInDataTypes.types(x.asText()))
          valueCopy = objectNode
        }
        case x:TextNode => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", BuiltInDataTypes.types("string"))
          valueCopy = objectNode
          warnings = warnings :+ PropertyChecker.invalidValueWarning
        }
      }
      val objectNode = valueCopy.asInstanceOf[ObjectNode]
      if (!objectNode.path("base").isMissingNode) {
        val baseValue = objectNode.get("base").asText()
        if (!PropertyCheckerConstants.StringDataTypes.contains(baseValue) || !PropertyCheckerConstants.BinaryDataTypes.contains(baseValue)) {
          if (!objectNode.path("length").isMissingNode) {
            throw new MetadataError(s"datatypes based on $baseValue cannot have a length facet")
          }
          if (!objectNode.path("minLength").isMissingNode) {
            throw new MetadataError(s"datatypes based on $baseValue cannot have a minLength facet")
          }
          if (!objectNode.path("maxLength").isMissingNode) {
            throw new MetadataError(s"datatypes based on $baseValue cannot have a maxLength facet")
          }
        }
      }

      if (!objectNode.path("minimum").isMissingNode) {
        objectNode.put("minInclusive", valueCopy.get("minimum").asText())
        objectNode.remove("minimum")
      }

      if (!objectNode.path("maximum").isMissingNode) {
        objectNode.put("maxInclusive", valueCopy.get("maximum").asText())
        objectNode.remove("maximum")
      }

      val baseValue = if (objectNode.path("base").isMissingNode) {
        ""
      } else {
        objectNode.get("base").asText
      }

      warnings :+ convertValueFacet(objectNode, "minInclusive", baseValue)
      warnings :+ convertValueFacet(objectNode, "minExclusive", baseValue)
      warnings :+ convertValueFacet(objectNode, "maxInclusive", baseValue)
      warnings :+ convertValueFacet(objectNode, "maxExclusive", baseValue)

      val minInclusive:Option[Either[Int, String]] = if (valueCopy.path("minInclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("minInclusive").isMissingNode) {
          None
        } else {
          Option(Left(objectNode.get("minInclusive").asInt()))
        }
      } else {
        if (objectNode.path("minInclusive").path("dateTime").isMissingNode) {
          None
        } else {
          Option(Right(objectNode.get("minInclusive").get("dateTime").asText()))
        }
      }

      val maxInclusive:Option[Either[Int, String]] = if (objectNode.path("maxInclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("maxInclusive").isMissingNode) {
          None
        } else {
          Option(Left(objectNode.get("maxInclusive").asInt()))
        }
      } else {
        if (objectNode.path("maxInclusive").path("dateTime").isMissingNode) {
          None
        } else {
          Option(Right(objectNode.get("maxInclusive").get("dateTime").asText()))
        }
      }

      val minExclusive:Option[Either[Int, String]] = if (objectNode.path("minExclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("minExclusive").isMissingNode) {
          None
        } else {
          Option(Left(objectNode.get("minExclusive").asInt()))
        }
      } else {
        if (objectNode.get("minExclusive").path("dateTime").isMissingNode) {
          None
        } else {
          Option(Right(objectNode.get("minExclusive").get("dateTime").asText()))
        }
      }

      val maxExclusive:Option[Either[Int, String]] = if (objectNode.path("maxExclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("maxExclusive").isMissingNode) {
          None
        } else {
          Option(Left(objectNode.get("maxExclusive").asInt()))
        }
      } else {
        if (objectNode.path("maxExclusive").path("dateTime").isMissingNode) {
          None
        } else {
          Option(Right(objectNode.get("maxExclusive").get("dateTime").asText()))
        }
      }

     (minInclusive, minExclusive, maxInclusive, maxExclusive) match {
       case (Some(Left(minI)), Some(Left(minE)), _, _) => throw new MetadataError(s"datatype cannot specify both minimum/minInclusive ($minI) and minExclusive ($minE)")
       case (Some(Right(minI)), Some(Right(minE)), _, _) => throw new MetadataError(s"datatype cannot specify both minimum/minInclusive ($minI) and minExclusive ($minE)")
       case (_, _, Some(Left(maxI)), Some(Left(maxE))) => throw new MetadataError(s"datatype cannot specify both maximum/maxInclusive ($maxI) and maxExclusive ($maxE)")
       case (_, _, Some(Right(maxI)), Some(Right(maxE))) => throw new MetadataError(s"datatype cannot specify both maximum/maxInclusive ($maxI) and maxExclusive ($maxE)")
       case (Some(Left(minI)), _,Some(Left(maxI)), _) if minI > maxI => throw new MetadataError(s"datatype minInclusive ($minI) cannot be more than maxInclusive ($maxI)")
       case (Some(Right(minI)), _,Some(Right(maxI)), _) if minI.asInstanceOf[Int] > maxI.asInstanceOf[Int] => throw new MetadataError(s"datatype minInclusive ($minI) cannot be more than maxInclusive ($maxI)")
       case (Some(Left(minI)), _, _, Some(Left(maxE))) if minI >= maxE => throw new MetadataError(s"datatype minInclusive ($minI) cannot be greater than or equal to maxExclusive ($maxE)")
       case (Some(Right(minI)), _, _, Some(Right(maxE))) if minI.asInstanceOf[Int] >= maxE.asInstanceOf[Int] => throw new MetadataError(s"datatype minInclusive ($minI) cannot be greater than or equal to maxExclusive ($maxE)")
       case (_, Some(Left(minE)), _, Some(Left(maxE))) if minE > maxE => throw new MetadataError(s"datatype minExclusive ($minE) cannot be greater than or equal to maxExclusive ($maxE)")
       case (_, Some(Right(minE)), _, Some(Right(maxE))) if minE.asInstanceOf[Int] > maxE.asInstanceOf[Int] => throw new MetadataError(s"datatype minExclusive ($minE) cannot be greater than or equal to maxExclusive ($maxE)")
       case (_, Some(Left(minE)), Some(Left(maxI)), _) if minE >= maxI => throw new MetadataError(s"datatype minExclusive ($minE) cannot be greater than maxInclusive ($maxI)")
       case (_, Some(Right(minE)), Some(Right(maxI)), _) if minE.asInstanceOf[Int] >= maxI.asInstanceOf[Int] => throw new MetadataError(s"datatype minExclusive ($minE) cannot be greater than maxInclusive ($maxI)")

       case (None, None, None, None) => {}
     }

      val minLength = objectNode.path("minLength")
      val maxLength = objectNode.path("maxLength")
      val length = objectNode.path("length")
      if (!length.isMissingNode && !minLength.isMissingNode && length.asInt < minLength.asInt) {
        throw new MetadataError(s"datatype length ($length) cannot be less than minLength ($minLength)")
      }
      if (!length.isMissingNode && !maxLength.isMissingNode && length.asInt > maxLength.asInt) {
        throw new MetadataError(s"datatype length ($length) cannot be more than maxLength ($maxLength)")
      }
      if (!minLength.isMissingNode && !maxLength.isMissingNode && minLength.asInt > maxLength.asInt) {
        throw new MetadataError(s"datatype minLength ($minLength) cannot be more than maxLength ($maxLength)")
      }

      if (!objectNode.path("format").isMissingNode) {
        val baseValue = objectNode.get("base").asText()
        if (PropertyCheckerConstants.RegExpFormatDataTypes.contains(baseValue)) {
          try {
            // In ruby regexp is stored in format key. Also regexp validated. Determine how to handle this in scala
            // value["format"] = Regexp.new(value["format"])
            objectNode.set("format", new TextNode(objectNode.get("format").asText))
          } catch {
            case e: Exception => {
              objectNode.remove("format")
              warnings = warnings :+ "invalid_regex"
            }
          }
        } else if (PropertyCheckerConstants.NumericFormatDataTypes.contains(baseValue)) {
          throw new NotImplementedError() // Implement after adding NumberFormat class
        } else if (baseValue == "http://www.w3.org/2001/XMLSchema#boolean") {
          if (objectNode.get("format").isTextual) {
            val formatValues = objectNode.get("format").asText.split("""\|""")
            if (formatValues.length != 2) {
              objectNode.remove("format")
              warnings = warnings :+ "invalid_boolean_format"
            } else {
              val arrayNodeObject = JsonNodeFactory.instance.arrayNode()
              arrayNodeObject.add(formatValues(0))
              arrayNodeObject.add(formatValues(1))
              objectNode.replace("format", arrayNodeObject)
            }
          }
        } else if (PropertyCheckerConstants.DateFormatDataTypes.contains(baseValue)) {
          throw new NotImplementedError() // Implement after adding DateFormat class
        }
      }
       (objectNode, warnings, csvwPropertyType)
    }
  }
}