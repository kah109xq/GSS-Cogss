package CSVValidation
import scala.collection.mutable
import scala.util.matching.Regex
import scala.collection.mutable.HashMap

object PropertyChecker {
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


}

class PropertyChecker(property:String, value:Any, baseUrl:String, lang:String) {

  val Properties = HashMap(
    // Context Properties
    "@language" -> languageProperty(PropertyType.Context),
    "@base" -> linkProperty(PropertyType.Context),
    // common properties
    "@id" -> linkProperty(PropertyType.Common),
    // Notes to implement - figure out how to handle different types of values
    "notes" -> notesProperty(PropertyType.Common),
    "suppressOutput" -> booleanProperty(PropertyType.Common),
    "lang" -> languageProperty(PropertyType.Inherited),
  )

  def checkProperty(): (Any, Any, PropertyType.Value) = {
    // More conditions and logic to add here.
    val f = Properties(property)
    return f
  }



  def  checkCommonPropertyValue(value: Any, baseUrl: String, lang: String):(Any, Any) = {
    value match {
      case s:HashMap[String, Any] => {
        throw new NotImplementedError("to be implemented later")

//        var warnings =
//        for((p,v) <- s) {
//          p match {
//            case "@context"
//              throw new MetadataError("common property has @context property")
//            case "@list"
//              th
//          }
//        }
      }
      case s:String => {
        lang match {
          case "und" => return (s, null)
          case _ => {
            val h = HashMap(
              "@value" -> s, "@language" -> lang
            )
            return (h, null)
          }
        }
      }
      case s:Array[String] => throw new NotImplementedError("to be implemented later")
      case _ => throw new IllegalArgumentException(s"Unexcepted input of type ${value.getClass}")
    }
  }

  def booleanProperty(typeString:PropertyType.Value):(Boolean, Any, PropertyType.Value) = {
    return value match {
      case b: Boolean => (b, "", typeString)
      case _ => (false, "invalid_value", typeString)
    }
  }

  def notesProperty(typeString:PropertyType.Value):(Any, Any, PropertyType.Value) = {
    value match {
      case xs: Array[String] => {
        val (values, warnings) = xs.map(x => checkCommonPropertyValue(x, baseUrl, lang))
                                   .unzip
        return (values, warnings, typeString)
      }
      case _ => return (false, "invalid_value", typeString)
    }
  }

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
      case s: String if PropertyChecker.Bcp47LanguagetagRegExp.pattern.matcher(s).matches => (s, "", typeString)
      case _ => ("", "invalid_value", typeString)
    }
  }
}