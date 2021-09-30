import CSVValidation.MetadataError

import scala.util.matching.Regex
import scala.collection.mutable.HashMap

case class DateFormat(pattern: Option[String], dataType: Option[String]) {
  private var dataTypeClone:String = _
  private var regExp:Regex = _
  private var regExpString:String = _
  private var testPattern:String = _
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
  private val datePatternRegExp = HashMap(
    "yyyy-MM-dd" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")$$""".r,
    "yyyyMMdd" -> raw"""^$fields("yyyy")$fields("MM")$fields("dd")$$""".r,
    "dd-MM-yyyy" -> raw"""^$fields("dd")-$fields("MM")-$fields("yyyy")$$""".r,
    "d-M-yyyy" -> raw"""^$fields("d")-$fields("M")-$fields("yyyy")$$""".r,
    "MM-dd-yyyy" -> raw"""^$fields("MM")-$fields("dd")-$fields("yyyy")$$""".r,
    "M-d-yyyy" -> raw"""^$fields("M")-$fields("d")-$fields("yyyy")$$""".r,
    "dd/MM/yyyy" -> raw"""^$fields("dd")/$fields("MM")/$fields("yyyy")$$""".r,
    "d/M/yyyy" -> raw"""^$fields("d")/$fields("M")/$fields("yyyy")$$""".r,
    "MM/dd/yyyy" -> raw"""^$fields("MM")/$fields("dd")/$fields("yyyy")$$""".r,
    "M/d/yyyy" -> raw"""^$fields("M")/$fields("d")/$fields("yyyy")$$""".r,
    "dd.MM.yyyy" -> raw"""^$fields("dd").$fields("MM").$fields("yyyy")$$""".r,
    "d.M.yyyy" -> raw"""^$fields("d").$fields("M").$fields("yyyy")$$""".r,
    "MM.dd.yyyy" -> raw"""^$fields("MM").$fields("dd").$fields("yyyy")$$""".r,
    "M.d.yyyy" -> raw"""^$fields("M").$fields("d").$fields("yyyy")$$""".r
  )
  private val timePatternRegExp = HashMap(
    "HH:mm:ss" -> raw"""^$fields("HH"):$fields("mm"):(?<second>$fields("ss"))$$""".r,
    "HHmmss" -> raw"""^$fields("HH")$fields("mm")(?<second>$fields("ss"))$$""".r,
    "HH:mm" -> raw"""^$fields("HH"):$fields("mm")$$""".r,
    "HHmm" -> raw"""^$fields("HH")$fields("mm")$$""".r
  )
  private val dateTimePatternRegExp = HashMap(
    "yyyy-MM-ddTHH:mm:ss" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm"):(?<second>$fields("ss"))$$""".r,
    "yyyy-MM-ddTHH:mm" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm")$$""".r
  )
  private var defaultRegExp = HashMap(
    "http://www.w3.org/2001/XMLSchema#date" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#dateTime" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm"):(?<second>$fields("ss")(\.[0-9]+)?)$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#dateTimeStamp" -> raw"""^$fields("yyyy")-$fields("MM")-$fields("dd")T$fields("HH"):$fields("mm"):(?<second>$fields("ss")(\.[0-9]+)?)$fields("XXX")$$""".r,
    "http://www.w3.org/2001/XMLSchema#gDay" -> raw"""^---$fields("dd")$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#gMonth" -> raw"""$fields("MM")$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#gMonthDay" -> raw"""^--$fields("MM")-$fields("dd")$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#gYear" -> raw"""FIELDS["yyyy")$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#gYearMonth" -> raw"""^$fields("yyyy")-$fields("MM")$fields("XXX")?$$""".r,
    "http://www.w3.org/2001/XMLSchema#time" -> raw"""FIELDS["HH"):$fields("mm"):(?<second>$fields("ss")(\.[0-9]+)?)$fields("XXX")?$$""".r
  )

  (pattern, dataType) match {
    case (None, Some(dt)) => {
      regExp = defaultRegExp(dt)
      dataTypeClone = dt
    }
    case (Some(pattern), None) => {
      testPattern = pattern
      testPattern = testPattern.replaceAll("/S+/", "")
      val iterableMap = fields.toSeq.sortBy(_._1.length).reverse
      for ((k, v) <- iterableMap) testPattern.replaceAll(k, "")
      // Not sure what this string is..
      val matcher = "/[GyYuUrQqMLlwWdDFgEecahHKkjJmsSAzZOvVXx]/".r.pattern.matcher(testPattern)
      if (matcher.matches) {
        throw new MetadataError("Unrecognised date field symbols in date format")
      }
      regExp = datePatternRegExp(pattern)
      dataTypeClone = if (regExp == None) "http://www.w3.org/2001/XMLSchema#time" else "http://www.w3.org/2001/XMLSchema#date"
      if (regExp == None) regExp = timePatternRegExp(pattern)
      dataTypeClone = if (regExp == None) "http://www.w3.org/2001/XMLSchema#dateTime" else dataTypeClone
      if (regExp == None) regExp = dateTimePatternRegExp(pattern)
      if (regExp == None) {
        regExpString = pattern
        if (!(".*/HH/.*".r.pattern.matcher(regExpString).matches()) && ".*/yyyy/.*".r.pattern.matcher(regExpString).matches()) {
          dataTypeClone = "http://www.w3.org/2001/XMLSchema#date"
        }
        if (".*/HH/.*".r.pattern.matcher(regExpString).matches() && !(".*/yyyy/.*".r.pattern.matcher(regExpString).matches())) {
          dataTypeClone = "http://www.w3.org/2001/XMLSchema#time"
        }
        if (".*/HH/.*".r.pattern.matcher(regExpString).matches() && ".*/yyyy/.*".r.pattern.matcher(regExpString).matches()) {
          dataTypeClone = "http://www.w3.org/2001/XMLSchema#dateTime"
        }

        regExpString = regExpString.replaceFirst("HH", fields("HH").toString())
        regExpString = regExpString.replaceFirst("mm", fields("mm").toString())

        if ("/ss\\.S+/".r.pattern.matcher(pattern).matches()) {
          val maxFractionalSeconds = pattern.split(".").last.length()
          regExpString = (regExpString.replaceFirst("/ss\\.S+$/", raw"""(?<second>$fields("ss")(\\.[0-9]{1,$maxFractionalSeconds})?)"""))
        } else {
          regExpString = regExpString.replaceFirst("ss", raw"""(?<second>$fields("ss")""")
        }

        if (".*/yyyy/.*".r.pattern.matcher(regExpString).matches()) {
          regExpString = regExpString.replaceFirst("yyyy", fields("yyyy").toString)
          regExpString = regExpString.replaceFirst("MM", fields("MM").toString)
          regExpString = regExpString.replaceFirst("M", fields("M").toString)
          regExpString = regExpString.replaceFirst("dd", fields("dd").toString)
          regExpString = regExpString.replaceFirst("/d(?=[-T \\/\\.])/", fields("d").toString)
        }

        regExpString = regExpString.replaceFirst("XXX", fields("XXX").toString)
        regExpString = regExpString.replaceFirst("XX", fields("XX").toString)
        regExpString = regExpString.replaceFirst("X", fields("XXX").toString)
        regExpString = regExpString.replaceFirst("xxx", fields("xxx").toString)
        regExpString = regExpString.replaceFirst("xx", fields("xx").toString)
        regExpString = regExpString.replaceFirst("/x(?!:)/", fields("d").toString)

        regExp = raw"""^$regExpString$$""".r
      }
    }
  }

  def parse(value: String): HashMap[String, Any]= {
    throw new NotImplementedError()
  }

}