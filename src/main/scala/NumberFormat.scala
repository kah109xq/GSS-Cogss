package CSVValidation

import com.ibm.icu.text.DecimalFormat

case class NumberFormat(pattern:Option[String], groupChar: Option[Char] = None, decimalChar: Option[Char] = None) {
  private var format:String = _
  private var df:DecimalFormat = new DecimalFormat()
  private val decimalFormatSymbols = df.getDecimalFormatSymbols
  decimalFormatSymbols.setInfinity("INF")
  decimalChar match {
    case Some(c) => decimalFormatSymbols.setDecimalSeparator(c)
    case _ => {}
  }
  groupChar match {
    case Some(c) => decimalFormatSymbols.setGroupingSeparator(c)
    case _ => {}
  }
  df.setDecimalFormatSymbols(decimalFormatSymbols)
  def getFormat: String = format
  pattern match {
    case Some(p) => {
      df.applyPattern(p)
      format = p
    }
    case _ => {
      // Figure out what the default pattern should be
    }
  }

  def parse(value:String): Number = {
    df.parse(value)
  }

  def format(value:Number): String = {
    df.format(value)
  }
}
