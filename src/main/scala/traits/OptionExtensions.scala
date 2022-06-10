package CSVValidation
package traits

object OptionExtensions {
  implicit class OptionIfDefined[T](option: Option[T]) {

    def ifDefined(f: T => Unit): Unit = option.foreach(f)
  }

}
