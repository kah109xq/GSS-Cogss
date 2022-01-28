package CSVValidation

// This is only a skeleton for the core Validator class to be created. It is planned to accept the source csv and
// schema and return errors and warnings collection when validate method is invoked. Changes to this signature can be
// made in the due course or as per requirements
class Validator(val filePath: String) {
  def validate(): Either[String, Array[String]] = {
    val result = Schema.loadMetadataAndValidate(filePath)
    result match {
      case Right(tableGroup) =>
        Right(tableGroup.warnings.map(w => processWarnings(w)))
      case Left(errorMessage) => Left(errorMessage)
    }
  }

  private def processWarnings(errorMessage: ErrorMessage): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }
}
