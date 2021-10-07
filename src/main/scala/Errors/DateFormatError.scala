package Errors
final case class DateFormatError(private val message: String = "",
                               private val cause: Throwable = None.orNull) extends Exception(message, cause)