package CSVValidation

case class WarningsAndErrors(
    warnings: Array[ErrorMessage],
    errors: Array[ErrorMessage]
)
