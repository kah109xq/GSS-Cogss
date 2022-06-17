package CSVValidation

import CSVValidation.WarningsAndErrors.{Errors, Warnings}

object WarningsAndErrors {
  type Warnings = Array[WarningWithCsvContext]
  type Errors = Array[ErrorWithCsvContext]
}
case class WarningsAndErrors(
    warnings: Warnings = Array(),
    errors: Errors = Array()
)
