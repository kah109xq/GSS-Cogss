import scala.util.matching.Regex
import scala.collection.mutable.HashMap

case class DateFormat(pattern: String, dataType: Option[String] = None) {
  private var fields = HashMap(
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
  private var datePatternRegExp = HashMap(
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
  private var timePatternRegExp = HashMap(
    "HH:mm:ss" -> raw"""^$fields("HH"):$fields("mm"):(?<second>$fields("ss"))$$""".r,
    "HHmmss" -> raw"""^$fields("HH")$fields("mm")(?<second>$fields("ss"))$$""".r,
    "HH:mm" -> raw"""^$fields("HH"):$fields("mm")$$""".r,
    "HHmm" -> raw"""^$fields("HH")$fields("mm")$$""".r
  )
  private var dateTimePatternRegExp = HashMap(
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

  def parse(value: String): HashMap[String, Any]= {
    throw new NotImplementedError()
  }
}