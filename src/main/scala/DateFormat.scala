import CSVValidation.MetadataError
import com.ibm.icu.text.DateTimePatternGenerator.FormatParser
import com.ibm.icu.text.SimpleDateFormat

import scala.collection.mutable
import scala.util.matching.Regex
import scala.collection.mutable.HashMap
import java.util.Locale
import com.ibm.icu.text.{DateFormat, SimpleDateFormat}


case class DateFormat(pattern: Option[String], dataType: Option[String]) {
  private var `type`: String = _
  private var format: String = _
  private var regExp: Regex = _
  private var regExpString: String = _
  private var testPattern: String = _
  private val fields = HashMap(
    "yyyy" -> "/(?<year>-?([1-9][0-9]{3,}|0[0-9]{3}))/".r,
    "MM" -> "/(?<month>0[1-9]|1[0-2])/".r,
    "M" -> "/(?<month>[1-9]|1[0-2])/".r,
    "dd" -> "/(?<day>0[1-9]|[12][0-9]|3[01])/".r,
    "d" -> "/(?<day>[1-9]|[12][0-9]|3[01])/".r,
    "HH" -> "/(?<hour>[01][0-9]|2[0-3])/".r,
    "mm" -> "/(?<minute>[0-5][0-9])/".r,
    "ss" -> "/([0-6][0-9])/".r,
    "X" -> "/(?<timezone>Z|[-+]((0[0-9]|1[0-3])([0-5][0-9])?|14(00)?))/".r,
    "XX" -> "/(?<timezone>Z|[-+]((0[0-9]|1[0-3])[0-5][0-9]|1400))/".r,
    "XXX" -> "/(?<timezone>Z|[-+]((0[0-9]|1[0-3]):[0-5][0-9]|14:00))/".r,
    "x" -> "/(?<timezone>[-+]((0[0-9]|1[0-3])([0-5][0-9])?|14(00)?))/".r,
    "xx" -> "/(?<timezone>[-+]((0[0-9]|1[0-3])[0-5][0-9]|1400))/".r,
    "xxx" -> "/(?<timezone>[-+]((0[0-9]|1[0-3]):[0-5][0-9]|14:00))/".r
  )
  //  private val datePatternRegExp = HashMap(
  //    "yyyy-MM-dd" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")$$""".r,
  //    "yyyyMMdd" -> raw"""^$fields("yyyy")$fields("MM")$fields("dd")$$""".r,
  //    "dd-MM-yyyy" -> raw"""^$fields("dd")-$fields("MM")-$fields("yyyy")$$""".r,
  //    "d-M-yyyy" -> raw"""^$fields("d")-$fields("M")-$fields("yyyy")$$""".r,
  //    "MM-dd-yyyy" -> raw"""^$fields("MM")-$fields("dd")-$fields("yyyy")$$""".r,
  //    "M-d-yyyy" -> raw"""^$fields("M")-$fields("d")-$fields("yyyy")$$""".r,
  //    "dd/MM/yyyy" -> raw"""^$fields("dd")/$fields("MM")/$fields("yyyy")$$""".r,
  //    "d/M/yyyy" -> raw"""^$fields("d")/$fields("M")/$fields("yyyy")$$""".r,
  //    "MM/dd/yyyy" -> raw"""^$fields("MM")/$fields("dd")/$fields("yyyy")$$""".r,
  //    "M/d/yyyy" -> raw"""^$fields("M")/$fields("d")/$fields("yyyy")$$""".r,
  //    "dd.MM.yyyy" -> raw"""^$fields("dd").$fields("MM").$fields("yyyy")$$""".r,
  //    "d.M.yyyy" -> raw"""^$fields("d").$fields("M").$fields("yyyy")$$""".r,
  //    "MM.dd.yyyy" -> raw"""^$fields("MM").$fields("dd").$fields("yyyy")$$""".r,
  //    "M.d.yyyy" -> raw"""^$fields("M").$fields("d").$fields("yyyy")$$""".r
  //  )
  //  private val timePatternRegExp = HashMap(
  //    "HH:mm:ss" -> raw"""^$fields("HH"):$fields("mm"):(?<second>$fields("ss"))$$""".r,
  //    "HHmmss" -> raw"""^$fields("HH")$fields("mm")(?<second>$fields("ss"))$$""".r,
  //    "HH:mm" -> raw"""^$fields("HH"):$fields("mm")$$""".r,
  //    "HHmm" -> raw"""^$fields("HH")$fields("mm")$$""".r
  //  )
  //  private val dateTimePatternRegExp = HashMap(
  //    "yyyy-MM-ddTHH:mm:ss" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm"):(?<second>$fields("ss"))$$""".r,
  //    "yyyy-MM-ddTHH:mm" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm")$$""".r
  //  )
  private val xmlSchemaBaseUrl = "http://www.w3.org/2001/XMLSchema#"
  private var defaultRegExp = HashMap(
    s"${xmlSchemaBaseUrl}date" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}dateTime" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm"):(?<second>$fields("ss")(\.[0-9]+)?)$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}dateTimeStamp" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm"):(?<second>$fields("ss")(\.[0-9]+)?)$fields("XXX")$$""".r,
    s"${xmlSchemaBaseUrl}gDay" -> raw"""^---$fields("dd")$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}gMonth" -> raw"""$fields("MM")$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}gMonthDay" -> raw"""^--$fields("MM")-$fields("dd")$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}gYear" -> raw"""FIELDS["yyyy")$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}gYearMonth" -> raw"""^$fields("yyyy")-$fields("MM")$fields("XXX")?$$""".r,
    s"${xmlSchemaBaseUrl}time" -> raw"""FIELDS["HH"):$fields("mm"):(?<second>$fields("ss")(\.[0-9]+)?)$fields("XXX")?$$""".r
  )

  (pattern, dataType) match {
    case (None, Some(dt)) => {
      regExp = defaultRegExp(dt)
      `type` = dt
      format = "YYYY-MM-DDThh:mm:ss.sTZD"
    }
    case (Some(p), None) => {
      format = p
      ensureDateTimeFormatContainsRecognizedSymbols(p)
      regExpString = p
      val hoursRegExp = "HH".r
      val yearsRegExp = "yyyy".r
      val includesHours = hoursRegExp.pattern.matcher(regExpString).find()
      val includesYears = yearsRegExp.pattern.matcher(regExpString).find
      `type` = if (!includesHours && includesYears) {
        s"${xmlSchemaBaseUrl}date"
      } else if (includesHours && !(includesYears)) {
        s"${xmlSchemaBaseUrl}time"
      } else if (includesHours && includesYears) {
        s"${xmlSchemaBaseUrl}dateTime"
      } else throw new MetadataError("Dont know what exactly to perform in this case")

      //
      //      new SimpleDateFormat("yyyy-mm")
      //
      //      import com.ibm.icu.text.DateTimePatternGenerator
      //      val generator = DateTimePatternGenerator.getInstance(Locale.FRANCE)
      //
      //
      //      // get a pattern for an abbreviated month and day
      //      val pattern = generator.getBestPattern("MMMd")
      //
      //      val dfFrance = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE)
      //      dfFrance.parse("yyyy")
//      regExpString = regExpString.replaceFirst("HH", fields("HH").toString())
//      regExpString = regExpString.replaceFirst("mm", fields("mm").toString())
//
//      if ("ss\\.S+".r.pattern.matcher(p).find()) {
//        val maxFractionalSeconds = p.split(".").last.length()
//        regExpString = (regExpString.replaceFirst("ss\\.S+$", raw"""(?<second>$fields("ss")(\\.[0-9]{1,$maxFractionalSeconds})?)"""))
//      } else {
//        regExpString = regExpString.replaceFirst("ss", raw"""(?<second>$fields("ss")""")
//      }
//
//      if (yearsRegExp.pattern.matcher(regExpString).find()) {
//        regExpString = regExpString.replaceFirst("yyyy", fields("yyyy").toString)
//        regExpString = regExpString.replaceFirst("MM", fields("MM").toString)
//        regExpString = regExpString.replaceFirst("M", fields("M").toString)
//        regExpString = regExpString.replaceFirst("dd", fields("dd").toString)
//        regExpString = regExpString.replaceFirst("d(?=[-T \\/\\.])", fields("d").toString)
//      }
//
//      regExpString = regExpString.replaceFirst("XXX", fields("XXX").toString)
//      regExpString = regExpString.replaceFirst("XX", fields("XX").toString)
//      regExpString = regExpString.replaceFirst("X", fields("XXX").toString)
//      regExpString = regExpString.replaceFirst("xxx", fields("xxx").toString)
//      regExpString = regExpString.replaceFirst("xx", fields("xx").toString)
//      regExpString = regExpString.replaceFirst("x(?!:)", fields("d").toString)
//
//      regExp = s"^$regExpString$$".r
    }
  }

  /**
   * This function ensures that the pattern received does not contain symbols which this class does not
   * know to process. An exception is thrown when it finds a symbol outside the recognised list.
   *
   * @param pattern
   */
  private def ensureDateTimeFormatContainsRecognizedSymbols(pattern: String) = {
    var testPattern = pattern
    // Fractional sections are variable length so are dealt with outside of the `fields` map.
    testPattern = testPattern.replaceAll("/S+/", "")

    val iterableMap = fields.toSeq.sortBy(_._1.length).reverse
    for ((k, v) <- iterableMap) testPattern = testPattern.replaceAll(k, "")
    // Not sure what this string is..
    // http://www.unicode.org/reports/tr35/tr35-dates.html#Date_Field_Symbol_Table
    val matcher = "/[GyYuUrQqMLlwWdDFgEecahHKkjJmsSAzZOvVXx]/".r.pattern.matcher(testPattern)
    if (matcher.matches) {
      throw new MetadataError("Unrecognised date field symbols in date format")
    }
  }

//  def parse(value: String): HashMap[String, Any] = {
//    var valueToReturn = mutable.HashMap[String,Any]()
//    val matches = regExp.findAllIn(value)
//    if (matches.isEmpty) {
//      return HashMap()
//    }
//    for (name <- matches.groupNames) {
//      if (!matches.group(name).isBlank) {
//        name match {
//          case "timezone" => {
//            var tz = matches.group("timezone")
//            if (tz == "Z") tz = "+00:00"
//            if (tz.length == 3) tz = tz + ":00"
//            if (!".*:.*".r.pattern.matcher(tz).matches()) {
//              tz = s"" ////tz = "#{tz[0..2]}:#{tz[3..4]}" unless tz =~ /:/
//            }
//            valueToReturn += ("timezone" -> tz)
//          }
//          case "second" => {
//            valueToReturn += ("second" -> matches.group("second").toFloat)
//          }
//          case _ => {
//            valueToReturn += (name -> matches.group(name).toInt)
//          }
//        }
//      }
//    }
//
//    `type` match {
//      case "http://www.w3.org/2001/XMLSchema#date" => {
//        try {
////          figure out which date object to use
////          valueToReturn += ("dateTime" -> )
//        } catch {
//          case e: Exception => {
//            return HashMap() // return equivalent of nil
//          }
//        }
//      }
//      case "http://www.w3.org/2001/XMLSchema#dateTime" => {
//        try {
////          figure out which datetime object to use
//        } catch {
//          case e: Exception => {
//            return HashMap() // return equivalent of nil
//          }
//        }
//      }
//      case _ => {
////        value[:dateTime] = DateTime.new(value[:year] || 0, value[:month] || 1, value[:day] || 1, value[:hour] || 0, value[:minute] || 0, value[:second] || 0, value[:timezone] || "+00:00")
//
//      }
//    }
//
//    if(valueToReturn.contains("year")) {
//      if(valueToReturn.contains("month")) {
//        if(valueToReturn.contains("day")) {
//          if(valueToReturn.contains("hour")) {
////            dateTime
////            use scala equivalent of "#{format('%04d', value[:year])}-#{format('%02d', value[:month])}-#{format('%02d', value[:day])}T#{format('%02d', value[:hour])}:#{format('%02d', value[:minute] || 0)}:#{format('%02g', value[:second] || 0)}#{value[:timezone] ? value[:timezone].sub("+00:00", "Z") : ''}"
////            valueToReturn += ("string" -> )
//          } else {
////            date
////
//          }
//        } else {
////          gYearMonth
////
//        }
//      } else {
////        gYear
//      }
//    } else if(valueToReturn.contains("month")) {
//      if(valueToReturn.contains("day")) {
////        gMonthDay
////        value[:string] = "--#{format('%02d', value[:month])}-#{format('%02d', value[:day])}#{value[:timezone] ? value[:timezone].sub("+00:00", "Z") : ''}"
//      } else {
////          gMonth
//      }
//    } else if(valueToReturn.contains("day")) {
////      gDay
////
//    } else {
////q
//    }
//    valueToReturn
//  }

//  def parse(value: String): HashMap[String, Any] = {
//
//    val simpleDateFormatter = new SimpleDateFormat(pattern)
//    val date = simpleDateFormatter.parse(value)
//  }
}