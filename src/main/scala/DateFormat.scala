package CSVValidation

import CSVValidation.DateFormat.{
  RegExGroups,
  mapDataTypeToDefaultValueRegExGroups
}
import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.GregorianCalendar

import java.text.ParsePosition
import java.time.{
  LocalDate,
  LocalDateTime,
  LocalTime,
  Month,
  ZoneId,
  ZoneOffset,
  ZonedDateTime
}
import java.util.TimeZone
import java.util.regex.Matcher

object DateFormat {
  private val xmlSchemaBaseUrl = "http://www.w3.org/2001/XMLSchema#"

  val defaultFormats = Map(
    s"${xmlSchemaBaseUrl}date" -> "yyyy-MM-ddX",
    s"${xmlSchemaBaseUrl}dateTime" -> "yyyy-MM-dd'T'HH:mm:ss.SX",
    s"${xmlSchemaBaseUrl}dateTimeStamp" -> "yyyy-MM-dd'T'HH:mm:ss.SX",
    s"${xmlSchemaBaseUrl}gDay" -> "---ddX",
    s"${xmlSchemaBaseUrl}gMonth" -> "--MMX",
    s"${xmlSchemaBaseUrl}gMonthDay" -> "--MM-ddX",
    s"${xmlSchemaBaseUrl}gYear" -> "yyyyX",
    s"${xmlSchemaBaseUrl}gYearMonth" -> "yyyy-MMX",
    s"${xmlSchemaBaseUrl}time" -> "HH:mm:ss.SX"
  )

  object RegExGroups {
    val years = "years"
    val months = "months"
    val days = "days"
    val hours = "hours"
    val minutes = "minutes"
    val seconds = "seconds"
    val fractionalSeconds = "fractionalSeconds"
    val endOfDay = "endOfDay"
    val timeZone = "timeZone"
    val tzZulu = "tzZulu"
    val tzSign = "tzSign"
    val tzHours = "tzHours"
    val tzMinutes = "tzMinute"
    val tz14 = "tz14"

    val timeZoneRegExNamedGroups = Set(
      timeZone,
      tzZulu,
      tzSign,
      tzHours,
      tzMinutes,
      tz14
    )
  }

  val digit = "[0-9]"
  val yearGrp = s"(?<${RegExGroups.years}>-?([1-9]$digit{3,}|0$digit{3}))"
  val monthGrp = s"(?<${RegExGroups.months}>(0[1-9]|1[0-2]))"
  val dayGrp = s"(?<${RegExGroups.days}>(0[1-9]|[12]$digit|3[01]))"
  val hourGrp = s"(?<${RegExGroups.hours}>([01]$digit|2[0-3]))"
  val minutes = s"[0-5]$digit"
  val minuteGrp = s"(?<${RegExGroups.minutes}>$minutes)"
  val secondGrp =
    s"(?<${RegExGroups.seconds}>[0-5]$digit)(\\.(?<${RegExGroups.fractionalSeconds}>$digit+))?"
  val endOfDay = s"(?<${RegExGroups.endOfDay}>24:00:00(\\.0+)?)"
  val timeZone =
    s"(?<${RegExGroups.timeZone}>(?<${RegExGroups.tzZulu}>Z)|(?<${RegExGroups.tzSign}>[+-])" +
      s"((?<${RegExGroups.tzHours}>0$digit|1[0-3]):" +
      s"(?<${RegExGroups.tzMinutes}>$minutes)|(?<${RegExGroups.tz14}>14:00)))"

  val mapDataTypeToDefaultValueRegEx = Map(
    s"${xmlSchemaBaseUrl}dateTime" -> s"^$yearGrp-$monthGrp-($dayGrp)T($hourGrp:$minuteGrp:$secondGrp|$endOfDay)($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}date" -> s"^$yearGrp-$monthGrp-$dayGrp($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}dateTimeStamp" -> s"^$yearGrp-$monthGrp-($dayGrp)T($hourGrp:$minuteGrp:$secondGrp|$endOfDay)($timeZone)$$".r,
    s"${xmlSchemaBaseUrl}gDay" -> s"^---$dayGrp($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}gMonth" -> s"^--$monthGrp($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}gMonthDay" -> s"^--$monthGrp-$dayGrp($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}gYear" -> s"^$yearGrp($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}gYearMonth" -> s"^$yearGrp-$monthGrp($timeZone)?$$".r,
    s"${xmlSchemaBaseUrl}time" -> s"^($hourGrp:$minuteGrp:$secondGrp|$endOfDay)($timeZone)?$$".r
  )

  /**
    * Each XSD date/time datatype has a default format. `mapDataTypeToDefaultValueRegEx` contains regular expressions
    * describing these default formats.
    *
    * This map enumerates the named groups contained within the aforementioned regular expressions.
    */
  val mapDataTypeToDefaultValueRegExGroups = Map[String, Set[String]](
    s"${xmlSchemaBaseUrl}dateTime" -> Set(
      RegExGroups.years,
      RegExGroups.months,
      RegExGroups.days,
      RegExGroups.hours,
      RegExGroups.minutes,
      RegExGroups.seconds,
      RegExGroups.fractionalSeconds,
      RegExGroups.endOfDay
    ).union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}date" -> Set(RegExGroups.years, RegExGroups.months, RegExGroups.days)
      .union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}dateTimeStamp" -> Set(
      RegExGroups.years,
      RegExGroups.months,
      RegExGroups.days,
      RegExGroups.hours,
      RegExGroups.minutes,
      RegExGroups.seconds,
      RegExGroups.fractionalSeconds,
      RegExGroups.endOfDay
    ).union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}gDay" -> Set(RegExGroups.days)
      .union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}gMonth" -> Set(RegExGroups.months)
      .union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}gMonthDay" -> Set(RegExGroups.months, RegExGroups.days)
      .union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}gYear" -> Set(RegExGroups.years)
      .union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}gYearMonth" -> Set(RegExGroups.years, RegExGroups.months)
      .union(RegExGroups.timeZoneRegExNamedGroups),
    s"${xmlSchemaBaseUrl}time" -> Set(
      RegExGroups.hours,
      RegExGroups.minutes,
      RegExGroups.seconds,
      RegExGroups.fractionalSeconds,
      RegExGroups.endOfDay
    ).union(RegExGroups.timeZoneRegExNamedGroups)
  )
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

  val utcZoneId = TimeZone.getTimeZone("UTC").toZoneId

}
case class DateFormat(format: Option[String], dataType: String) {
  import CSVValidation.DateFormat.{
    defaultFormats,
    fields,
    mapDataTypeToDefaultValueRegEx,
    utcZoneId
  }

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
      case Some(_) => parseWithUts35FormatString(inputDate)
      case None    => parseWithDefaultFormatForDataType(inputDate)
    }
  }

  private def parseWithUts35FormatString(
      inputDate: String
  ): Option[ZonedDateTime] = {
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

  private def parseWithDefaultFormatForDataType(
      inputDate: String
  ): Option[ZonedDateTime] = {
    /*
     * We expect the datatype to be known, why wouldn't it be?
     * We cannot generate a UTS-35 format string which defines the default format for xsd date/time datatypes.
     * In this case we hand-craft regular expressions to help us validate these default formats and extract the
     * dates/times contained.
     */
    (
      mapDataTypeToDefaultValueRegEx.get(dataType),
      mapDataTypeToDefaultValueRegExGroups.get(dataType)
    ) match {
      case (Some(defaultValueRegex), Some(namedGroupsExpected)) => {
        val defaultFormatRegExMatcher = defaultValueRegex.pattern.matcher(inputDate)
        if (defaultFormatRegExMatcher.matches()) {
          try {
            val (localDate, localTime) = parseDateAndTimeForDefaultFormat(
              defaultFormatRegExMatcher,
              namedGroupsExpected
            )
            val timeZoneId = parseTimeZoneIdForDefaultFormat(
              defaultFormatRegExMatcher,
              namedGroupsExpected
            )

            Some(ZonedDateTime.of(localDate, localTime, timeZoneId))
          } catch { // If there are some errors, don't break program execution - return None
            // For example 04-31 is not a valid date since april has only 30 days. Invalid range exception will be
            // thrown here, but if the date is non parsable return None as the return type for this method is Option
            case e => None
            // todo: Ajay, might be a good idea to provide some logging here so we can debug when this happens.
          }
        } else {
          None
        }
      }
      case (None, _) =>
        throw DateFormatError(
          s"Unmatched datatype ${dataType} in `mapDataTypeToDefaultValueRegEx`"
        )
      case (_, None) =>
        throw DateFormatError(
          s"Unmatched datatype ${dataType} in `mapDataTypeToDefaultValueRegExGroups`"
        )
    }
  }

  private def getMaybeNamedGroup(
      defaultFormatRegExMatcher: Matcher,
      namedGroupsExpected: Set[String],
      groupName: String
  ): Option[String] = {
    if (namedGroupsExpected.contains(groupName)) {
      Option(defaultFormatRegExMatcher.group(groupName))
    } else {
      None
    }
  }

  private def parseDateAndTimeForDefaultFormat(
      defaultFormatRegExMatcher: Matcher,
      namedGroupsExpected: Set[String]
  ): (LocalDate, LocalTime) = {
    val years = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.years)
      .map(_.toInt)
      .getOrElse(0)
    val months = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.months)
      .map(_.toInt)
      .getOrElse(1)
    val days = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.days)
      .map(_.toInt)
      .getOrElse(1)
    val endOfDay = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected,RegExGroups.endOfDay)
    val (hours, minutes, seconds, nanoSeconds) =
      if (endOfDay.isDefined) {
        (24, 0, 0, 0)
      } else {
        val h = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.hours)
          .map(_.toInt)
          .getOrElse(0)
        val m = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.minutes)
          .map(_.toInt)
          .getOrElse(0)
        val s = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.seconds)
          .map(_.toInt)
          .getOrElse(0)
        val fs = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.fractionalSeconds)
          .map(f => s"0.$f".toDouble)
          .getOrElse(0.0)

        /*
         Let's not mark a date-time as invalid just because it uses more fractional digits than Java can handle. Let's
         just truncate the number of digits.

         The XSD:datetime datatype doesn't technically specify a limit on the number of fractional second digits
         which can be specified.
         Unfortunately, if the user is using date-times with more fractional second digits than java can cope with,
         we won't be able to successfully validate the uniqueness of primary key constraints.
         */
        val ns = (fs * 1e9).toInt
        (h, m, s, ns)
      }
    val localDate = LocalDate.of(years, Month.of(months), days)
    val localTime = LocalTime.of(hours, minutes, seconds, nanoSeconds)
    (localDate, localTime)
  }

  private def parseTimeZoneIdForDefaultFormat(
      defaultFormatRegExMatcher: Matcher,
      namedGroupsExpected: Set[String]
  ): ZoneId = {
    if (getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.timeZone).isEmpty) {
      utcZoneId
    } else if (getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.tzZulu).isDefined) {
      utcZoneId
    } else {
      val sign = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.tzSign)
        .map(s => if (s == "+") 1 else -1)
        .get

      val (tzHours, tzMinutes) =
        if (getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.tz14).isDefined) {
          (14, 0)
        } else {
          val tzHours = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.tzHours)
            .map(_.toInt)
            .getOrElse(0)
          val tzMinutes = getMaybeNamedGroup(defaultFormatRegExMatcher, namedGroupsExpected, RegExGroups.tzMinutes)
            .map(_.toInt)
            .getOrElse(0)

          (tzHours, tzMinutes)
        }

      val zoneOffset =
        ZoneOffset.ofTotalSeconds(sign * (tzHours * 60 + tzMinutes) * 60)
      ZoneId.ofOffset("UTC", zoneOffset)
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
      // Default timezone where the user's string does not specify one.
      com.ibm.icu.util.TimeZone.getTimeZone("UTC")
    )
    calendar.setLenient(false)
    simpleDateFormatter.setLenient(false)
    simpleDateFormatter.parse(inputDate, calendar, new ParsePosition(0))
    calendar
  }
}
