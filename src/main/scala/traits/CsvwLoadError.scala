package CSVValidation

/**
  * This is a base class for errors which occur when attempting to load a CSV-W schema file.
  */
abstract class CsvwLoadError {
  val error: Throwable
}
