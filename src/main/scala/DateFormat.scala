package CSVValidation

import CSVValidation.DateFormat.defaultFormats
import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.GregorianCalendar

import java.text.ParsePosition
import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util
import java.util.{Calendar, Date, TimeZone}

object DateFormat {
  val defaultFormats = Map(
    "http://www.w3.org/2001/XMLSchema#date" -> "yyyy-MM-ddXXX",
    "http://www.w3.org/2001/XMLSchema#dateTime" -> "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
    "http://www.w3.org/2001/XMLSchema#dateTimeStamp" -> "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
    "http://www.w3.org/2001/XMLSchema#gDay" -> "---ddXXX",
    "http://www.w3.org/2001/XMLSchema#gMonth" -> "--MMXXX",
    "http://www.w3.org/2001/XMLSchema#gMonthDay" -> "--MM-ddXXX",
    "http://www.w3.org/2001/XMLSchema#gYear" -> "yyyyXXX",
    "http://www.w3.org/2001/XMLSchema#gYearMonth" -> "yyyy-MMXXX",
    "http://www.w3.org/2001/XMLSchema#time" -> "HH:mm:ss.SSSXXX"
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
      simpleDateFormatter = new SimpleDateFormat(format)
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

  def parse(inputDate: String): Option[ZonedDateTime] = {
    val parsedDateTime = parseInputDateTimeRetainTimeZoneInfo(inputDate)
    simpleDateFormatter.setTimeZone(parsedDateTime.getTimeZone)

    val formattedDate = simpleDateFormatter.format(parsedDateTime.getTime)
    val formatPreservesAllInformation = formattedDate == inputDate

    if (formatPreservesAllInformation) {
      Some(getZonedDateTimeForIcuCalendar(parsedDateTime))
    } else {
      None
    }
  }

  private def getZonedDateTimeForIcuCalendar(parsedDateTime: GregorianCalendar): ZonedDateTime = {
    val zoneOffset = ZoneOffset.ofTotalSeconds(parsedDateTime.getTimeZone.getRawOffset / 1000)
    val dateTime = LocalDateTime.ofEpochSecond(
      parsedDateTime.getTimeInMillis / 1000,
      ((parsedDateTime.getTimeInMillis % 1000) * 1e6).toInt,
      zoneOffset
    )

    ZonedDateTime.ofLocal(
      dateTime,
      ZoneId.ofOffset("UTC", zoneOffset),
      zoneOffset
    )
  }

  private def parseInputDateTimeRetainTimeZoneInfo(inputDate: String) = {
    val calendar = new GregorianCalendar()
    calendar.setLenient(false)
    simpleDateFormatter.setLenient(false)
    simpleDateFormatter.parse(inputDate, calendar, new ParsePosition(0))
    calendar
  }
}
