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
  private var hasUnquotedPlusMinus = false
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
        val (sanitisedPattern, unquotedPM) = something(p)
        hasUnquotedPlusMinus = unquotedPM
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

  def getHasUnquotedPlusMinusSign(): Boolean = hasUnquotedPlusMinus

  def something(format: String): (String, Boolean) = {
    var sanitisedFormat = new StringBuilder()
    var insideQuotes = false
    var hasUnquotedPlus = false
    var hasUnquotedMinus = false
    for (char <- format) {
      if (char == '\'') insideQuotes = !insideQuotes
      if (char == '+' && !insideQuotes) {
        if (hasUnquotedPlus) {
          throw new IllegalArgumentException(
            "Not sure how to process this scenario"
          )
        }
        sanitisedFormat.append(char)
        hasUnquotedPlus = true
      } else if (char == '-' && !insideQuotes) {
        if (hasUnquotedMinus) {
          throw new IllegalArgumentException(
            "Not sure how to process this scenario"
          )
        }
        sanitisedFormat.append("+")

        hasUnquotedMinus = true
      } else {
        sanitisedFormat.append(char)
      }
    }
    (sanitisedFormat.toString(), hasUnquotedPlus || hasUnquotedMinus)
  }
}
