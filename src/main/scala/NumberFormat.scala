package CSVValidation

import com.ibm.icu.text.DecimalFormat

case class NumberFormat(pattern:Option[String]) {
  private var format:String = _
  private var df:DecimalFormat = new DecimalFormat()
  def getFormat: String = format
  pattern match {
    case Some(p) => {
      df.applyPattern(p)
      format = p
    }
    case _ => {
      // Figure out what the default pattern should be
      df.toString
    }
  }

  def parse(value:String): Number = {
    df.parse(value)
  }

  def format(value:Number): String = {
    df.format(value)
  }
}
