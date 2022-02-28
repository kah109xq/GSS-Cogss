package CSVValidation

import CSVValidation.DateFormat.defaultFormats
import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.{
  BasicTimeZone,
  Calendar,
  GregorianCalendar,
  SimpleTimeZone,
  TimeZone
}

import java.util.Date

object DateFormat {
  val defaultFormats = Map(
    "http://www.w3.org/2001/XMLSchema#date" -> "yyyy-MM-ddX",
    "http://www.w3.org/2001/XMLSchema#dateTime" -> "yyyy-MM-dd'T'HH:mm:ss.SX",
    "http://www.w3.org/2001/XMLSchema#dateTimeStamp" -> "yyyy-MM-dd'T'HH:mm:ss.SX",
    "http://www.w3.org/2001/XMLSchema#gDay" -> "---ddX",
    "http://www.w3.org/2001/XMLSchema#gMonth" -> "--MMX",
    "http://www.w3.org/2001/XMLSchema#gMonthDay" -> "--MM-ddX",
    "http://www.w3.org/2001/XMLSchema#gYear" -> "yyyyX",
    "http://www.w3.org/2001/XMLSchema#gYearMonth" -> "yyyy-MMX",
    "http://www.w3.org/2001/XMLSchema#time" -> "HH:mm:ss.SX"
  )
}
case class DateFormat(pattern: Option[String], dataType: Option[String]) {
  private var format: String = _
  private val fields = Array[String](
    "yyyy",
    "M",
    "MM",
    "dd",
    "d",
    "HH",
    "mm",
    "ss",
    "X",
    "XX",
    "XXX",
    "x",
    "xx",
    "xxx"
  )
  private val xmlSchemaBaseUrl = "http://www.w3.org/2001/XMLSchema#"
  private var simpleDateFormatter: SimpleDateFormat = _
  def getFormat() = format

  (pattern, dataType) match {
    case (None, Some(dt)) => {
      format = defaultFormats(dt)
//      simpleDateFormatter = new SimpleDateFormat(format)
      simpleDateFormatter = new SimpleDateFormat()
      simpleDateFormatter.applyLocalizedPattern(format)
    }
    case (Some(p), _) => {
      format = p
      simpleDateFormatter = new SimpleDateFormat(format)
      ensureDateTimeFormatContainsRecognizedSymbols(p)
    }
    case (None, None) =>
      throw new DateFormatError("Pattern or datatype must be specified")
  }

  /**
    * This function ensures that the pattern received does not contain symbols which this class does not
    * know to process. An exception is thrown when it finds a symbol outside the recognised list.
    *
    * @param pattern
    */
  private def ensureDateTimeFormatContainsRecognizedSymbols(pattern: String) = {
    var testPattern = pattern
    // Fractional sections are variable length so are dealt with outside of the `fields` map.
    testPattern = testPattern.replaceAll("/S+/", "")

    val fieldsLongestFirst = fields.sortBy(_.length).reverse
    for (k <- fieldsLongestFirst) testPattern = testPattern.replaceAll(k, "")
    // http://www.unicode.org/reports/tr35/tr35-dates.html#Date_Field_Symbol_Table
    val matcher =
      "[GyYuUrQqMLlwWdDFgEecahHKkjJmsSAzZOvVXx]".r.pattern.matcher(testPattern)
    if (matcher.find()) {
      throw new DateFormatError(
        "Unrecognised date field symbols in date format"
      )
    }
  }

  def parse(inputDate: String): Option[Date] = {
    val timeZoneForInputDate =
      simpleDateFormatter.getTimeZoneFormat.parse("-05:00")
    simpleDateFormatter.setTimeZone(timeZoneForInputDate)
    val date = simpleDateFormatter.parse(inputDate)
    val formattedDate = simpleDateFormatter.format(date)
    if (formattedDate == inputDate) {
      // If the `formattedDate` is exactly equal to the `inputDate`, then we know that the inputDate matches
      // the `format` exactly.
      Some(date)
    } else {
      None
    }
  }
}
