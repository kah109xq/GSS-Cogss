package CSVValidation

import CSVValidation.DateFormat.{defaultFormats, mapDataTypeToDefaultValueRegEx}
import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.{GregorianCalendar, ULocale}

import java.text.ParsePosition
import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}
import java.util
import java.util.{Calendar, Date, TimeZone}

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

  val digit = "[0-9]"
  val year = s"-?([1-9]$digit{3,}|0$digit{3})"
  val month = "(0[1-9]|1[0-2])"
  val day = s"(0[1-9]|[12]$digit|3[01])"
  val hour = s"([01]$digit|2[0-3])"
  val minute = s"[0-5]$digit"
  val second = s"[0-5]$digit(\\.$digit+)?"
  val endOfDay = "24:00:00(\\.0+)?"
  val timeZone = s"Z|[+-]((0$digit|1[0-3]):$minute|14:00)"

  val mapDataTypeToDefaultValueRegEx = Map(
    "http://www.w3.org/2001/XMLSchema#dateTime" -> s"^$year-$month-${day}T($hour:$minute:$second|$endOfDay)($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#date" -> s"^$year-$month-$day($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#dateTimeStamp" -> s"^$year-$month-${day}T($hour:$minute:$second|$endOfDay)($timeZone)$$".r,
    "http://www.w3.org/2001/XMLSchema#gDay" -> s"^---$day($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#gMonth" -> s"^--$month($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#gMonthDay" -> s"^--$month-$day($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#gYear" -> s"^$year($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#gYearMonth" -> s"^$year-$month($timeZone)?$$".r,
    "http://www.w3.org/2001/XMLSchema#time" -> s"^($hour:$minute:$second|$endOfDay)($timeZone)?$$".r
  )

}
case class DateFormat(format: Option[String], dataType: String) {
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

  format match {
    case Some(f) => {
      simpleDateFormatter = new SimpleDateFormat(f)
      ensureDateTimeFormatContainsRecognizedSymbols(f)
    }
    case None => {
      simpleDateFormatter = new SimpleDateFormat(defaultFormats(dataType))
    }
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
    testPattern = testPattern.replaceAll("S+", "")
    testPattern = testPattern.replaceAll("'.+'", "")

    "yyyy-MM-dd 'Hello, \'Rob\' - are you alright?'"

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
    format match {
      case Some(_) => {
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
      case None => {
        // We expect the datatype to be known, why wouldn't it be?
        // We cannot generate a UTS-35 format string which defines the default format for xsd date/time datatypes.
        // In this case we hand-craft regular expressions to help us validate these default formats and extract the
        // dates/times contained.

        mapDataTypeToDefaultValueRegEx.get(dataType) match {
          case Some(defaultValueRegex) => {
            val matcher = defaultValueRegex.pattern.matcher(inputDate)
            if (matcher.matches()) {
              val parsedDateTime = parseInputDateTimeRetainTimeZoneInfo(
                inputDate
              )
              Some(getZonedDateTimeForIcuCalendar(parsedDateTime))
            } else {
              None
            }
          }
          case None =>
            throw new DateFormatError(s"Unmatched datatype ${dataType}")
        }
      }
    }
  }

  private def getZonedDateTimeForIcuCalendar(
      parsedDateTime: GregorianCalendar
  ): ZonedDateTime = {
    val zoneOffset =
      ZoneOffset.ofTotalSeconds(parsedDateTime.getTimeZone.getRawOffset / 1000)
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
    val calendar = new GregorianCalendar(
      com.ibm.icu.util.TimeZone.getTimeZone("UTC")
    )
    calendar.setLenient(false)
    simpleDateFormatter.setLenient(false)
    simpleDateFormatter.parse(inputDate, calendar, new ParsePosition(0))
    calendar
  }
}
