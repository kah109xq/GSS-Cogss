package CSVValidation

case class WarningsAndErrors(
    warnings: Array[ErrorWithCsvContext],
    errors: Array[ErrorWithCsvContext]
)
