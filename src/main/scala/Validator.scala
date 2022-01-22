package CSVValidation

// This is only a skeleton for the core Validator class to be created. It is planned to accept the source csv and
// schema and return errors and warnings collection when validate method is invoked. Changes to this signature can be
// made in the due course or as per requirements
class Validator(val schemaUri: String) {
  def validate(outputErrors: Boolean): (Array[String], Option[String]) = {
    val result = Schema.loadMetadataAndValidate(schemaUri)
    val schema = result._1
    val errorMessage = result._2
    var warnings = Array[String]()
    schema match {
      case Some(tableGroup) => {
        if (outputErrors) {
          warnings =
            warnings.concat(tableGroup.warnings.map(w => processWarnings(w)))
        }
        (warnings, errorMessage)
      }
      case None => (warnings, errorMessage)
    }
  }

  private def processWarnings(errorMessage: ErrorMessage): String = {
    s"Type: ${errorMessage.`type`}, Category: ${errorMessage.category}, " +
      s"Row: ${errorMessage.row}, Column: ${errorMessage.column}, " +
      s"Content: ${errorMessage.content}, Constraints: ${errorMessage.constraints} \n"
  }
}
