package CSVValidation
final case class NumberFormatError(
    message: String = "",
    cause: Throwable = None.orNull
) extends Exception(message, cause)
