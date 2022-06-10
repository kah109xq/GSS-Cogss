package CSVValidation

import CSVValidation.WarningsAndErrors.{Errors, Warnings}

object WarningsAndErrors {
  type Warnings = Array[ErrorWithCsvContext]
  type Errors = Array[ErrorWithCsvContext]
}
case class WarningsAndErrors(
    warnings: Warnings = Array(),
    errors: Errors = Array()
) {}
