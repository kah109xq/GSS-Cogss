package CSVValidation

case class SchemaDoesNotContainCsvError(error: Throwable) extends CsvwLoadError
