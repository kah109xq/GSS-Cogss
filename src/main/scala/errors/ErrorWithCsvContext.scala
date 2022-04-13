package CSVValidation

case class ErrorWithCsvContext(
    `type`: String,
    category: String,
    row: String,
    column: String,
    content: String,
    constraints: String
) {}
