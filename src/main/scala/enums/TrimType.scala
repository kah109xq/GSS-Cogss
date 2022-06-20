package CSVValidation

object TrimType extends Enumeration {
  val True, False, Start, End = Value

  def fromString(name: String): Value =
    values.find(_.toString.toLowerCase() == name.toLowerCase()).get
}
