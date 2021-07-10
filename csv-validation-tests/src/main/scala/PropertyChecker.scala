package CSVValidation
import scala.collection.mutable
import scala.util.matching.Regex
import scala.collection.mutable.HashMap

class PropertyChecker(property:String, value:Any, baseUrl:String, lang:String) {

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

  val Properties = HashMap(
    // Context Properties
    "@language" -> languageProperty(PropertyType.Context),
    "@base" -> linkProperty(PropertyType.Context),
    // common properties
    //    "@id" -> linkProperty("common"),
    // Notes to implement - figure out how to handle different types of values
    "suppressOutput" -> booleanProperty(PropertyType.Common)
  )

  def checkProperty(): (Any, Any, PropertyType.Value) = {
    // More conditions and logic to add here.
    val f = Properties(property)
    return f
  }



  def checkCommonPropertyValue() = {
    // To be implemented
  }

  def booleanProperty(typeString:PropertyType.Value):(Boolean, Any, PropertyType.Value) = {
    return value match {
      case b: Boolean => (b, "", typeString)
      case _ => (false, "invalid_value", typeString)
    }
  }

//  def notesProperty():(Object, String, String) = {
//    if (value instanceof Array) {
//      return ("false", "invalid_value", "common")
//    }
//
//
//  }

  def linkProperty(typeString:PropertyType.Value):(String, Any, PropertyType.Value) = {
    var baseUrlCopy = ""
    value match {
      case s: String => {
        val matcher = "^_:.*".r.pattern.matcher(s)
        if(matcher.matches) {
          throw new MetadataError(s"URL $s starts with _:")
        }
        baseUrlCopy = baseUrl match {
          case "" => s
          case _ => baseUrl + s
        }
        return (baseUrlCopy, "", typeString)
      }
      case _ => return("", "invalid_value", typeString)
    }
  }

  def languageProperty(typeString:PropertyType.Value):(String, Any, PropertyType.Value) = {
    return value match {
      case s: String if Bcp47LanguagetagRegExp.pattern.matcher(s).matches => (s, "", typeString)
      case _ => ("", "invalid_value", typeString)
    }
  }
}