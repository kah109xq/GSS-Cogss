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
    "xxx" -> "/(?<timezone>[-+]((0[0-9]|1[0-3]):[0-5][0-9]|14:00))/".r)

  def parse(value: String): HashMap[String, Any]= {
    throw new NotImplementedError()
  }
}