package CSVValidation
case class ErrorMessage(
    `type`: String,
    category: String,
    row: String,
    column: String,
    content: String,
    constraints: String
) {}
