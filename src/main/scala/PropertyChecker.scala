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


  val StringDataTypes = Array[String](
  "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral",
  "http://www.w3.org/1999/02/22-rdf-syntax-ns#HTML",
  "http://www.w3.org/ns/csvw#JSON",
  "http://www.w3.org/2001/XMLSchema#string",
  "http://www.w3.org/2001/XMLSchema#normalizedString",
  "http://www.w3.org/2001/XMLSchema#token",
  "http://www.w3.org/2001/XMLSchema#language",
  "http://www.w3.org/2001/XMLSchema#Name",
  "http://www.w3.org/2001/XMLSchema#NMTOKEN"
  )

  val BinaryDataTypes = Array[String](
  "http://www.w3.org/2001/XMLSchema#base64Binary",
  "http://www.w3.org/2001/XMLSchema#hexBinary"
  )

  val IntegerFormatDataTypes = Array[String](
  "http://www.w3.org/2001/XMLSchema#integer",
  "http://www.w3.org/2001/XMLSchema#long",
  "http://www.w3.org/2001/XMLSchema#int",
  "http://www.w3.org/2001/XMLSchema#short",
  "http://www.w3.org/2001/XMLSchema#byte",
  "http://www.w3.org/2001/XMLSchema#nonNegativeInteger",
  "http://www.w3.org/2001/XMLSchema#positiveInteger",
  "http://www.w3.org/2001/XMLSchema#unsignedLong",
  "http://www.w3.org/2001/XMLSchema#unsignedInt",
  "http://www.w3.org/2001/XMLSchema#unsignedShort",
  "http://www.w3.org/2001/XMLSchema#unsignedByte",
  "http://www.w3.org/2001/XMLSchema#nonPositiveInteger",
  "http://www.w3.org/2001/XMLSchema#negativeInteger"
  )

  val NumericFormatDataTypes = Array[String](
  "http://www.w3.org/2001/XMLSchema#decimal",
  "http://www.w3.org/2001/XMLSchema#double",
  "http://www.w3.org/2001/XMLSchema#float"
  ) :+ IntegerFormatDataTypes

  val DateFormatDataTypes = Array[String](
  "http://www.w3.org/2001/XMLSchema#date",
  "http://www.w3.org/2001/XMLSchema#dateTime",
  "http://www.w3.org/2001/XMLSchema#dateTimeStamp",
  "http://www.w3.org/2001/XMLSchema#time"
  )

  val RegExpFormatDataTypes = Array[String](
  "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral",
  "http://www.w3.org/1999/02/22-rdf-syntax-ns#HTML",
  "http://www.w3.org/ns/csvw#JSON",
  "http://www.w3.org/2001/XMLSchema#anyAtomicType",
  "http://www.w3.org/2001/XMLSchema#anyURI",
  "http://www.w3.org/2001/XMLSchema#base64Binary",
  "http://www.w3.org/2001/XMLSchema#duration",
  "http://www.w3.org/2001/XMLSchema#dayTimeDuration",
  "http://www.w3.org/2001/XMLSchema#yearMonthDuration",
  "http://www.w3.org/2001/XMLSchema#hexBinary",
  "http://www.w3.org/2001/XMLSchema#QName",
  "http://www.w3.org/2001/XMLSchema#string",
  "http://www.w3.org/2001/XMLSchema#normalizedString",
  "http://www.w3.org/2001/XMLSchema#token",
  "http://www.w3.org/2001/XMLSchema#language",
  "http://www.w3.org/2001/XMLSchema#Name",
  "http://www.w3.org/2001/XMLSchema#NMTOKEN"
  )

  val BuiltInTypes = Array[String]("TableGroup", "Table", "Schema", "Column", "Dialect", "Template", "Datatype")

  val NameRegExp = "^([A-Za-z0-9]|(%[A-F0-9][A-F0-9]))([A-Za-z0-9_]|(%[A-F0-9][A-F0-9]))*$".r

  val Properties = Map(
    "@language" -> languageProperty (PropertyType.Context),
       // Context Properties
//       ,Properties
       "@base" -> linkProperty(PropertyType.Context),
//       // common properties
       "@id" -> linkProperty(PropertyType.Common),
//       // Notes to implement - figure out how to handle different types of values
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

//  JsonNode - primitive values + ObjectNodes + ArrayNode
//  ObjectNode
//  ArrayNode

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
      if(PropertyChecker.DateFormatDataTypes.contains(datatype)) {
        throw new NotImplementedError("To be implemented after implementing DateFormat class")
      } else if (PropertyChecker.NumericFormatDataTypes.contains(datatype)) {
        return Array[String]()
      } else {
        throw new MetadataError(s"$property is only allowed for numeric, date/time and duration types")
      }
    }
    return Array[String]()
  }

  def booleanProperty(typeString:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
    return (value, baseUrl, lang) => {
      if(value.isBoolean) {
        (value, Array[String](), typeString)
      } else {
        (BooleanNode.getFalse, Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def stringProperty(typeString: PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
    return (value, baseUrl, lang) => {
      if (value.isTextual) {
         (value, Array[String](), typeString)
      } else {
         (new TextNode(""), Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def numericProperty(typeString: PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     return (value, baseUrl, lang) => {
      if (value.isInt && value.asInt >= 0) {
         (value, Array[String](), typeString)
      } else if (value.isInt && value.asInt < 0) {
         (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), typeString)
      } else {
         (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def notesProperty(typeString:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     def notesPropertyInternal (value: JsonNode, baseUrl: String, lang: String): (JsonNode, Array[String], PropertyType.Value) = {
      if (value.isArray) {
        val arrayValue = value.asInstanceOf[ArrayNode]
        val elements = arrayValue.elements().asScala
        if (elements forall (_.isTextual())) {
          /**
           * [(1,2), (3,4), (5,6)] => ([1,3,5], [2,4,6])
           */
          val (values, warnings) = Array.from(elements.map(x => checkCommonPropertyValue(x, baseUrl, lang))).unzip
          val arrayNode:ArrayNode = PropertyChecker.mapper.valueToTree(values)
          return (arrayNode, warnings, typeString)
        }
      }
       (BooleanNode.getFalse, Array[String](PropertyChecker.invalidValueWarning), typeString)
    }
    return notesPropertyInternal
  }

  def nullProperty(typeString:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
      if (value.isTextual) {
         (value, Array[String](), typeString)
      } else if (value.isArray) {
        var values = Array[String]()
        var warnings = Array[String]()
        for (x <- value.elements().asScala) {
          x match {
            case xs if xs.isTextual => values = values :+ xs.asText()
            case _ => warnings = warnings :+ PropertyChecker.invalidValueWarning
          }
        }
        val arrayNode:ArrayNode = PropertyChecker.mapper.valueToTree(values)
         (arrayNode, warnings, typeString)
      } else {
         (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def separatorProperty(typeString:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
       value match {
        case s if s.isTextual => (s, null, typeString)
        case s if s.isNull => (s, null, typeString)
        case _ => (NullNode.getInstance(), Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def linkProperty(typeString:PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (v, baseUrl, lang) => {
      var baseUrlCopy = ""
      v match {
        case s if s.isTextual => {
          val matcher = PropertyChecker.startsWithUnderscore.pattern.matcher(s.asText())
          if (matcher.matches) {
            throw new MetadataError(s"URL ${s.asText} starts with _:")
          }
          baseUrlCopy = baseUrl match {
            case "" => s.asText()
            case _ => baseUrl + s.asText()
          }
           (new TextNode(baseUrlCopy), Array[String](""), typeString)
        }
        case _ =>  (new TextNode(""), Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def languageProperty (typeString:PropertyType.Value) : (JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
      value match {
        case s if s.isTextual && PropertyChecker.Bcp47LanguagetagRegExp.pattern.matcher(s.asText()).matches => (s, Array[String](), typeString)
        case _ => (new TextNode(""), Array[String](PropertyChecker.invalidValueWarning), typeString)
      }
    }
  }

  def datatypeProperty(typeString: PropertyType.Value):(JsonNode, String, String) => (JsonNode, Array[String], PropertyType.Value) = {
     (value, baseUrl, lang) => {
      var warnings = Array[String]()
      var valueCopy = value.deepCopy()
        .asInstanceOf[JsonNode] // Just coz its a json node
      valueCopy match {
        case v if v.isObject => {
          val objectNode = v.asInstanceOf[ObjectNode]
          if (!objectNode.path("@id").isMissingNode) {
            val idValue = objectNode.get("@id").asText()
            if (BuiltInDataTypes.types.contains(idValue)) { // check this coz contains just checks in keys
              throw new MetadataError(s"datatype @id must not be the id of a built-in datatype ($idValue)")
            } else {
              val (_, w, _) = linkProperty(PropertyType.Common)(objectNode.get("@id"), baseUrl, lang) // assign warnings in val w
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
        case x if x.isTextual && (BuiltInDataTypes.types.contains(x.asText())) => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", BuiltInDataTypes.types(x.asText()))
          valueCopy = objectNode
        }
        case x if x.isTextual => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", BuiltInDataTypes.types("string"))
          valueCopy = objectNode
          warnings = warnings :+ PropertyChecker.invalidValueWarning
        }
      }
      val objectNode = valueCopy.asInstanceOf[ObjectNode]
      if (!objectNode.path("base").isMissingNode) {
        val baseValue = objectNode.get("base").asText()
        if (!PropertyChecker.StringDataTypes.contains(baseValue) || !PropertyChecker.BinaryDataTypes.contains(baseValue)) {
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

      val minInclusive = if (valueCopy.path("minInclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("minInclusive").isMissingNode) {
          null
        } else {
          objectNode.get("minInclusive").asInt()
        }
      } else {
        if (objectNode.path("minInclusive").path("dateTime").isMissingNode) {
          null
        } else {
          objectNode.get("minInclusive").get("dateTime").asText()
        }
      }

      val maxInclusive = if (objectNode.path("maxInclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("maxInclusive").isMissingNode) {
          null
        } else {
          objectNode.get("maxInclusive").asInt()
        }
      } else {
        if (objectNode.path("maxInclusive").path("dateTime").isMissingNode) {
          null
        } else {
          objectNode.get("maxInclusive").get("dateTime").asText()
        }
      }

      val minExclusive = if (objectNode.path("minExclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("minExclusive").isMissingNode) {
          null
        } else {
          objectNode.get("minExclusive").asInt()
        }
      } else {
        if (objectNode.get("minExclusive").path("dateTime").isMissingNode) {
          null
        } else {
          objectNode.get("minExclusive").get("dateTime").asText()
        }
      }

      val maxExclusive = if (objectNode.path("maxExclusive").path("dateTime").isMissingNode) {
        if (objectNode.path("maxExclusive").isMissingNode) {
          null
        } else {
          objectNode.get("maxExclusive").asInt()
        }
      } else {
        if (objectNode.path("maxExclusive").path("dateTime").isMissingNode) {
          null
        } else {
          objectNode.get("maxExclusive").get("dateTime").asText()
        }
      }


      if (minInclusive != null && minExclusive != null) {
        throw new MetadataError(s"datatype cannot specify both minimum/minInclusive ($minInclusive) and minExclusive ($minExclusive)")
      }

      if (maxInclusive != null && maxExclusive != null) {
        throw new MetadataError(s"datatype cannot specify both maximum/maxInclusive ($maxInclusive) and maxExclusive ($maxExclusive)")
      }

      if (minInclusive != null && maxInclusive != null && minInclusive.asInstanceOf[Int] > maxInclusive.asInstanceOf[Int]) {
        throw new MetadataError(s"datatype minInclusive ($minInclusive) cannot be more than maxInclusive ($maxInclusive)")
      }

      if (minInclusive != null && maxExclusive != null && minInclusive.asInstanceOf[Int] >= maxExclusive.asInstanceOf[Int]) {
        throw new MetadataError(s"datatype minInclusive ($minInclusive) cannot be greater than or equal to maxExclusive ($maxExclusive)")
      }

      if (minExclusive != null && maxExclusive != null && minExclusive.asInstanceOf[Int] > maxExclusive.asInstanceOf[Int]) {
        throw new MetadataError(s"datatype minExclusive ($minExclusive) cannot be greater than or equal to maxExclusive ($maxExclusive)")
      }

      if (minExclusive != null && maxInclusive != null && minExclusive.asInstanceOf[Int] >= maxInclusive.asInstanceOf[Int]) {
        throw new MetadataError(s"datatype minExclusive ($minExclusive) cannot be greater than maxInclusive ($maxInclusive)")
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
        if (PropertyChecker.RegExpFormatDataTypes.contains(baseValue)) {
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
        } else if (PropertyChecker.NumericFormatDataTypes.contains(baseValue)) {
          throw new NotImplementedError() // Implement after adding NumberFormat class
        } else if (baseValue == "http://www.w3.org/2001/XMLSchema#boolean") {
          if (objectNode.get("format").isTextual) {
            val formatValues = objectNode.get("format").asText.split("""\|""")
            if (formatValues.length != 2) {
              objectNode.remove("format")
              warnings = warnings :+ "invalid_boolean_format"
            } else {
              // Use a better way to create arrayNodeObject
              val arrayNodeObject = JsonNodeFactory.instance.arrayNode()
              arrayNodeObject.add(formatValues(0))
              arrayNodeObject.add(formatValues(1))
              objectNode.replace("format", arrayNodeObject)
            }
          }
        } else if (PropertyChecker.DateFormatDataTypes.contains(baseValue)) {
          throw new NotImplementedError() // Implement after adding DateFormat class
        }
      }
       (objectNode, warnings, typeString)
    }
  }
}