package CSVValidation

import com.ibm.icu.text.DecimalFormat

case class NumberFormat(
    pattern: Option[String],
    groupChar: Option[Char] = None,
    decimalChar: Option[Char] = None
) {

  /**
    * Raising an exception when a pattern is supplied as we cannot figure out how to actually use them in some scenarios.
    */
  if (pattern.isDefined)
    throw MetadataError("Format supplied for numeric data-types")

  private val df: DecimalFormat = new DecimalFormat()
  private val decimalFormatSymbols = df.getDecimalFormatSymbols

  decimalFormatSymbols.setInfinity("INF")
  try {
    decimalChar match {
      case Some(c) => decimalFormatSymbols.setDecimalSeparator(c)
      // If no decimal separator is specified, set it to . (dot)
      // Setting it since we are not sure of the locale the user running this program will have, and might cause issues
      // if it is not the default decimal separator .
      case _ => decimalFormatSymbols.setDecimalSeparator('.')
    }
    groupChar match {
      case Some(c) => decimalFormatSymbols.setGroupingSeparator(c)
      // If no grouping separator is specified, set it to , (comma)
      case _ => decimalFormatSymbols.setGroupingSeparator(',')
    }
    df.setDecimalFormatSymbols(decimalFormatSymbols)
    pattern match {
      case Some(p) => {
        df.applyPattern(p)
        df.setParseStrict(true)
      }
      case _ => {
        // Figure out what the default pattern should be
      }
    }
  } catch {
    case e: Exception => throw new NumberFormatError(e.getMessage, e)
  }

  def parse(value: String): Number = {
    df.parse(value)
  }

  def format(value: Number): String = {
    df.format(value)
  }
}
