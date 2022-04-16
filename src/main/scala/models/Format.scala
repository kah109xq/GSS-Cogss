package CSVValidation

case class Format(
    maybeBooleanFormatOrRegExFormat: Option[String],
    pattern: Option[String],
    decimalChar: Option[Char],
    groupChar: Option[Char]
)
