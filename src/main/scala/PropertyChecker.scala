package CSVValidation
import CSVValidation.PropertyType.Context
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.{ArrayNode, JsonNodeFactory, ObjectNode, TextNode}

import scala.collection.IterableOnce.iterableOnceExtensionMethods
import scala.jdk.CollectionConverters._
import scala.util.matching.Regex
import scala.collection.mutable.HashMap
import com.fasterxml.jackson.databind.ObjectMapper


object PropertyChecker {
  val mapper = new ObjectMapper
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

  val BuiltInDataTypes = HashMap(
    "number" -> "http://www.w3.org/2001/XMLSchema#double",
    "binary" -> "http://www.w3.org/2001/XMLSchema#base64Binary",
    "datetime" -> "http://www.w3.org/2001/XMLSchema#dateTime",
    "any" -> "http://www.w3.org/2001/XMLSchema#anyAtomicType",
    "xml" -> "http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral",
    "html" -> "http://www.w3.org/1999/02/22-rdf-syntax-ns#HTML",
    "json" -> "http://www.w3.org/ns/csvw#JSON",
    "anyAtomicType" -> "http://www.w3.org/2001/XMLSchema#anyAtomicType",
    "anyURI" -> "http://www.w3.org/2001/XMLSchema#anyURI",
    "base64Binary" -> "http://www.w3.org/2001/XMLSchema#base64Binary",
    "boolean" -> "http://www.w3.org/2001/XMLSchema#boolean",
    "date" -> "http://www.w3.org/2001/XMLSchema#date",
    "dateTime" -> "http://www.w3.org/2001/XMLSchema#dateTime",
    "dateTimeStamp" -> "http://www.w3.org/2001/XMLSchema#dateTimeStamp",
    "decimal" -> "http://www.w3.org/2001/XMLSchema#decimal",
    "integer" -> "http://www.w3.org/2001/XMLSchema#integer",
    "long" -> "http://www.w3.org/2001/XMLSchema#long",
    "int" -> "http://www.w3.org/2001/XMLSchema#int",
    "short" -> "http://www.w3.org/2001/XMLSchema#short",
    "byte" -> "http://www.w3.org/2001/XMLSchema#byte",
    "nonNegativeInteger" -> "http://www.w3.org/2001/XMLSchema#nonNegativeInteger",
    "positiveInteger" -> "http://www.w3.org/2001/XMLSchema#positiveInteger",
    "unsignedLong" -> "http://www.w3.org/2001/XMLSchema#unsignedLong",
    "unsignedInt" -> "http://www.w3.org/2001/XMLSchema#unsignedInt",
    "unsignedShort" -> "http://www.w3.org/2001/XMLSchema#unsignedShort",
    "unsignedByte" -> "http://www.w3.org/2001/XMLSchema#unsignedByte",
    "nonPositiveInteger" -> "http://www.w3.org/2001/XMLSchema#nonPositiveInteger",
    "negativeInteger" -> "http://www.w3.org/2001/XMLSchema#negativeInteger",
    "double" -> "http://www.w3.org/2001/XMLSchema#double",
    "duration" -> "http://www.w3.org/2001/XMLSchema#duration",
    "dayTimeDuration" -> "http://www.w3.org/2001/XMLSchema#dayTimeDuration",
    "yearMonthDuration" -> "http://www.w3.org/2001/XMLSchema#yearMonthDuration",
    "float" -> "http://www.w3.org/2001/XMLSchema#float",
    "gDay" -> "http://www.w3.org/2001/XMLSchema#gDay",
    "gMonth" -> "http://www.w3.org/2001/XMLSchema#gMonth",
    "gMonthDay" -> "http://www.w3.org/2001/XMLSchema#gMonthDay",
    "gYear" -> "http://www.w3.org/2001/XMLSchema#gYear",
    "gYearMonth" -> "http://www.w3.org/2001/XMLSchema#gYearMonth",
    "hexBinary" -> "http://www.w3.org/2001/XMLSchema#hexBinary",
    "QName" -> "http://www.w3.org/2001/XMLSchema#QName",
    "string" -> "http://www.w3.org/2001/XMLSchema#string",
    "normalizedString" -> "http://www.w3.org/2001/XMLSchema#normalizedString",
    "token" -> "http://www.w3.org/2001/XMLSchema#token",
    "language" -> "http://www.w3.org/2001/XMLSchema#language",
    "Name" -> "http://www.w3.org/2001/XMLSchema#Name",
    "NMTOKEN" -> "http://www.w3.org/2001/XMLSchema#NMTOKEN",
    "time" -> "http://www.w3.org/2001/XMLSchema#time"
  )

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

  val NameSpaces = HashMap(
    "dcat" -> "http://www.w3.org/ns/dcat#",
    "qb" -> "http://purl.org/linked-data/cube#",
    "grddl" -> "http://www.w3.org/2003/g/data-view#",
    "ma" -> "http://www.w3.org/ns/ma-ont#",
    "org" -> "http://www.w3.org/ns/org#",
    "owl" -> "http://www.w3.org/2002/07/owl#",
    "prov" -> "http://www.w3.org/ns/prov#",
    "rdf" -> "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
    "rdfa" -> "http://www.w3.org/ns/rdfa#",
    "rdfs" -> "http://www.w3.org/2000/01/rdf-schema#",
    "rif" -> "http://www.w3.org/2007/rif#",
    "rr" -> "http://www.w3.org/ns/r2rml#",
    "sd" -> "http://www.w3.org/ns/sparql-service-description#",
    "skos" -> "http://www.w3.org/2004/02/skos/core#",
    "skosxl" -> "http://www.w3.org/2008/05/skos-xl#",
    "wdr" -> "http://www.w3.org/2007/05/powder#",
    "void" -> "http://rdfs.org/ns/void#",
    "wdrs" -> "http://www.w3.org/2007/05/powder-s#",
    "xhv" -> "http://www.w3.org/1999/xhtml/vocab#",
    "xml" -> "http://www.w3.org/XML/1998/namespace",
    "xsd" -> "http://www.w3.org/2001/XMLSchema#",
    "csvw" -> "http://www.w3.org/ns/csvw#",
    "cnt" -> "http://www.w3.org/2008/content",
    "earl" -> "http://www.w3.org/ns/earl#",
    "ht" -> "http://www.w3.org/2006/http#",
    "oa" -> "http://www.w3.org/ns/oa#",
    "ptr" -> "http://www.w3.org/2009/pointers#",
    "cc" -> "http://creativecommons.org/ns#",
    "ctag" -> "http://commontag.org/ns#",
    "dc" -> "http://purl.org/dc/terms/",
    "dcterms" -> "http://purl.org/dc/terms/",
    "dc11" -> "http://purl.org/dc/elements/1.1/",
    "foaf" -> "http://xmlns.com/foaf/0.1/",
    "gr" -> "http://purl.org/goodrelations/v1#",
    "ical" -> "http://www.w3.org/2002/12/cal/icaltzd#",
    "og" -> "http://ogp.me/ns#",
    "rev" -> "http://purl.org/stuff/rev#",
    "sioc" -> "http://rdfs.org/sioc/ns#",
    "v" -> "http://rdf.data-vocabulary.org/#",
    "vcard" -> "http://www.w3.org/2006/vcard/ns#",
    "schema" -> "http://schema.org/"
    )
}

class PropertyChecker(property:String, value: JsonNode, baseUrl:String, lang:String) {

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

  def checkProperty(): (Any, Any, PropertyType.Value) = {
    // More conditions and logic to add here.
    val f = Properties(property)
    return f()
  }

//  JsonNode - primitive values + ObjectNodes + ArrayNode
//  ObjectNode
//  ArrayNode

  def checkCommonPropertyValue(value: JsonNode, baseUrl: String, lang: String):(Any, Any) = {
    if(value.isObject) {
      throw new NotImplementedError("to be implemented later")
    }
    else if(value.isTextual) {
      val s = value.asText()
      lang match {
        case "und" => return (s, null)
        case _ => {
          val h = HashMap(
            "@value" -> s, "@language" -> lang
          )
          return (h, null)
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

  def booleanProperty(typeString:PropertyType.Value):() => (Boolean, Any, PropertyType.Value) = {
    return () => {
      if(value.isBoolean) {
        (value.asBoolean(), "", typeString)
      } else {
        (false, "invalid_value", typeString)
      }
    }
  }

  def stringProperty(typeString: PropertyType.Value):() => (String, Any, PropertyType.Value) = {
    return () => {
      if (value.isTextual) {
         (value.asText(), null, typeString)
      } else {
         ("", "invalid_value", typeString)
      }
    }
  }

  def numericProperty(typeString: PropertyType.Value):() => (Any, Any, PropertyType.Value) = {
     return () => {
      if (value.isInt && value.asInt >= 0) {
         (value.asInt(), null, typeString)
      } else if (value.isInt && value.asInt < 0) {
         (null, "invalid_value", typeString)
      } else {
         (null, "invalid_value", typeString)
      }
    }
  }

  def notesProperty(typeString:PropertyType.Value):() => (Any, Any, PropertyType.Value) = {
     def notesPropertyInternal (): (Any, Any, PropertyType.Value) = {
      if (value.isArray) {
        val arrayValue = value.asInstanceOf[ArrayNode]
        val elements = arrayValue.elements().asScala
        if (elements forall (_.isTextual())) {
          /**
           * [(1,2), (3,4), (5,6)] => ([1,3,5], [2,4,6])
           */
          val (values, warnings) = Array.from(elements.map(x => checkCommonPropertyValue(x, baseUrl, lang))).unzip
          return (values, warnings, typeString)
        }
      }
       (false, "invalid_value", typeString)
    }
    return notesPropertyInternal
  }

  def nullProperty(typeString:PropertyType.Value):() => (Array[String], Any, PropertyType.Value) = {
     () => {
      if (value.isTextual) {
         (Array[String](value.asText()), null, typeString)
      } else if (value.isArray) {
        var values = Array[String]()
        var warnings = Array[String]()
        for (x <- value.elements().asScala) {
          x match {
            case xs if xs.isTextual => values = values :+ xs.asText()
            case _ => warnings = warnings :+ "invalid_value"
          }
        }
         (values, warnings, typeString)
      } else {
         (Array[String](""), "invalid_value", typeString)
      }
    }
  }

  def separatorProperty(typeString:PropertyType.Value):() => (Any, Any, PropertyType.Value) = {
     () => {
       value match {
        case s if s.isTextual => (s.asText, null, typeString)
        case s if s.isNull => (s, null, typeString)
        case _ => (null, "invalid_value", typeString)
      }
    }
  }

  def linkProperty(typeString:PropertyType.Value, v:JsonNode = value):() => (String, String, PropertyType.Value) = {
     () => {
      var baseUrlCopy = ""
      v match {
        case s if s.isTextual => {
          val matcher = "^_:.*".r.pattern.matcher(s.asText())
          if (matcher.matches) {
            throw new MetadataError(s"URL ${s.asText} starts with _:")
          }
          baseUrlCopy = baseUrl match {
            case "" => s.asText()
            case _ => baseUrl + s.asText()
          }
           (baseUrlCopy, "", typeString)
        }
        case _ =>  ("", "invalid_value", typeString)
      }
    }
  }

  def languageProperty (typeString:PropertyType.Value) : () => (String, Any, PropertyType.Value) = {
     () => {
      value match {
        case s if s.isTextual && PropertyChecker.Bcp47LanguagetagRegExp.pattern.matcher(s.asText()).matches => (s.asText(), "", typeString)
        case _ => ("", "invalid_value", typeString)
      }
    }
  }

  def datatypeProperty(typeString: PropertyType.Value):() => (Any, Any, PropertyType.Value) = {
     () => {
      var warnings = Array[String]()
      var valueCopy = value.deepCopy()
        .asInstanceOf[JsonNode] // Just coz its a json node
      valueCopy match {
        case v if v.isObject => {
          val objectNode = v.asInstanceOf[ObjectNode]
          if (!objectNode.path("@id").isMissingNode) {
            val idValue = objectNode.get("@id").asText()
            if (PropertyChecker.BuiltInDataTypes.contains(idValue)) { // check this coz contains just checks in keys
              throw new MetadataError(s"datatype @id must not be the id of a built-in datatype ($idValue)")
            } else {
              val (_, w, _) = linkProperty(PropertyType.Common, objectNode.get("@id"))() // assign warnings in val w
              if (w != "") {
                warnings = warnings :+ w
                objectNode.remove("@id")
              }
            }
          }

          if (!objectNode.path("base").isMissingNode) {
            val baseValue = objectNode.get("base").asText()
            if (PropertyChecker.BuiltInDataTypes.contains(baseValue)) {
              objectNode.put("base", PropertyChecker.BuiltInDataTypes(baseValue))
            } else {
              objectNode.put("base", PropertyChecker.BuiltInDataTypes("string"))
              warnings = warnings :+ "invalid_datatype_base"
            }
          } else {
            objectNode.put("base", PropertyChecker.BuiltInDataTypes("string"))
          }
        }
        case x if x.isTextual && (PropertyChecker.BuiltInDataTypes.contains(x.asText())) => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", PropertyChecker.BuiltInDataTypes(x.asText()))
          valueCopy = objectNode
        }
        case x if x.isTextual => {
          val objectNode = PropertyChecker.mapper.createObjectNode
          objectNode.put("@id", PropertyChecker.BuiltInDataTypes("string"))
          valueCopy = objectNode
          warnings = warnings :+ "invalid_value"
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
          objectNode.get("minInclusive").asText()
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
          objectNode.get("maxInclusive").asText()
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
          objectNode.get("minExclusive").asText()
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
          objectNode.get("maxExclusive").asText()
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

      if (minInclusive != null && maxInclusive != null && minInclusive > maxInclusive) {
        throw new MetadataError(s"datatype minInclusive ($minInclusive) cannot be more than maxInclusive ($maxInclusive)")
      }

      if (minInclusive != null && maxExclusive != null && minInclusive >= maxExclusive) {
        throw new MetadataError(s"datatype minInclusive $minInclusive) cannot be more than or equal to maxExclusive ($maxExclusive)")
      }

      if (minExclusive != null && maxExclusive != null && minExclusive > maxExclusive) {
        throw new MetadataError(s"datatype minExclusive ($minExclusive) cannot be more than or equal to maxExclusive ($maxExclusive)")
      }

      if (minExclusive != null && maxInclusive != null && minExclusive >= maxInclusive) {
        throw new MetadataError(s"datatype minExclusive ($minExclusive) cannot be more than maxInclusive ($maxInclusive)")
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
              warnings :+ "invalid_regex"
            }
          }
        } else if (PropertyChecker.NumericFormatDataTypes.contains(baseValue)) {
          throw new NotImplementedError() // Implement after adding NumberFormat class
        } else if (baseValue == "http://www.w3.org/2001/XMLSchema#boolean") {
          if (objectNode.get("format").isTextual) {
            val formatValues = objectNode.get("format").asText.split(" ")
            if (formatValues.length != 2) {
              objectNode.remove("format")
              warnings :+ "invalid_boolean_format"
            } else {
              // Use a better way to create arrayNodeObject
              val arrayNodeObject = JsonNodeFactory.instance.arrayNode()
              arrayNodeObject.add(formatValues(0))
              arrayNodeObject.add(formatValues(1))
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