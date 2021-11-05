package CSVValidation

import Errors.NumberFormatError
import com.ibm.icu.text.DecimalFormat

case class NumberFormat(pattern:Option[String], groupChar: Option[Char] = None, decimalChar: Option[Char] = None) {
  private var df:DecimalFormat = new DecimalFormat()
  private val decimalFormatSymbols = df.getDecimalFormatSymbols
  decimalFormatSymbols.setInfinity("INF")
  try {
    decimalChar match {
      case Some(c) => decimalFormatSymbols.setDecimalSeparator(c)
      case _ => {}
    }
    groupChar match {
      case Some(c) => decimalFormatSymbols.setGroupingSeparator(c)
      case _ => {}
    }
    df.setDecimalFormatSymbols(decimalFormatSymbols)
    pattern match {
      case Some(p) => {
        df.applyPattern(p)
      }
      case _ => {
        // Figure out what the default pattern should be
      }
    }
  } catch {
    case e:Exception => throw new NumberFormatError(e.getMessage)
  }

  def parse(value:String): Number = {
    df.parse(value)
  }

  def format(value:Number): String = {
    df.format(value)
  }
}
