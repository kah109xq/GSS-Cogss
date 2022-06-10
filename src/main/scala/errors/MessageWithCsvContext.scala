package CSVValidation

/**
 * Represents an abstract Error/Warning to be displayed to the user.
 */
abstract class MessageWithCsvContext {
  def `type`: String
  def category: String
  def row: String
  def column: String
  def content: String
  def constraints: String
}

case class ErrorWithCsvContext(
    `type`: String,
    category: String,
    row: String,
    column: String,
    content: String,
    constraints: String
) extends MessageWithCsvContext {}

case class WarningWithCsvContext(
  `type`: String,
  category: String,
  row: String,
  column: String,
  content: String,
  constraints: String
) extends MessageWithCsvContext {}
