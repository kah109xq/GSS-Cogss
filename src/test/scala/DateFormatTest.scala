package CSVValidation
import org.scalatest.FunSuite

import java.time.Month

class DateFormatTest extends FunSuite {
  test(
    "it throws an exception for unrecognized date field symbols in date format"
  ) {
    val thrown = intercept[DateFormatError] {
      DateFormat(
        Some("yyyy-MM-ddUNKNOWN"),
        "http://www.w3.org/2001/XMLSchema#dateTime"
      )
    }
    assert(
      thrown.getMessage === "Unrecognised date field symbols in date format"
    )
  }

  test(
    "the provided format should override the default format for the datatype"
  ) {
    val dateFormatObj =
      DateFormat(
        Some("yyyy-MM-dd'T'HH:mm:ss.SXXX'RandomTextHere'"),
        "http://www.w3.org/2001/XMLSchema#dateTime"
      )
    val maybeParsedDateTime = dateFormatObj.parse(
      "2002-10-10T12:00:00.1-05:00RandomTextHere"
    )

    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getYear == 2002)
    assert(parsedDateTime.getMonthValue == 10)
    assert(parsedDateTime.getDayOfMonth == 10)
    assert(parsedDateTime.getHour == 12)
    assert(parsedDateTime.getMinute == 0)
    assert(parsedDateTime.getSecond == 0)
    assert(parsedDateTime.getNano == 1 * 1e8)
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
  }

  test(
    "when format is not provided, it should parse the datetime provided based on the datatype"
  ) {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#dateTime")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2002-10-10T12:00:00.0123456-05:00"
    )

    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getYear == 2002)
    assert(parsedDateTime.getMonthValue == 10)
    assert(parsedDateTime.getDayOfMonth == 10)
    assert(parsedDateTime.getHour == 12)
    assert(parsedDateTime.getMinute == 0)
    assert(parsedDateTime.getSecond == 0)
    assert(parsedDateTime.getNano == 0.0123456 * 1e9)
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
  }

  test("it should parse datetime without timezone information") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#dateTime")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2002-10-10T12:00:00.0123456"
    )

    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getYear == 2002)
    assert(parsedDateTime.getMonthValue == 10)
    assert(parsedDateTime.getDayOfMonth == 10)
    assert(parsedDateTime.getHour == 12)
    assert(parsedDateTime.getMinute == 0)
    assert(parsedDateTime.getSecond == 0)
    assert(parsedDateTime.getNano == 0.0123456 * 1e9)
    assert(parsedDateTime.getOffset.getTotalSeconds == 0)
  }

  test("it should parse gDay") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gDay")
    val maybeParsedDateTime = dateFormatObj.parse(
      "---01-04:00"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getDayOfMonth == 1)
    assert(parsedDateTime.getOffset.getTotalSeconds == -4 * 60 * 60)

    assert(parsedDateTime.getYear == 0)
    assert(parsedDateTime.getMonth == Month.JANUARY)
  }

  test("it should parse gMonth") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gMonth")
    val maybeParsedDateTime = dateFormatObj.parse(
      "--04-05:00"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getMonthValue == 4)
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
  }

  test("it should not return gMonth for invalid values") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gMonth")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2004-04" //the year must not be specified
    )
    assert(maybeParsedDateTime.isLeft)
  }

  test("it should parse gMonthDay") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gMonthDay")
    val maybeParsedDateTime = dateFormatObj.parse(
      "--04-12Z"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getMonthValue == 4)
    assert(parsedDateTime.getOffset.getTotalSeconds == 0)
  }

  test("it should not return gMonthDay for invalid values") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gMonthDay")
    val maybeParsedDateTime = dateFormatObj.parse(
      "--04-31" //it must be a valid day of the year (April has 30 days)
    )
    assert(maybeParsedDateTime.isLeft)
  }

  test("it should parse gYear") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gYear")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2004-05:00"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getYear == 2004)
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
  }

  test("it should not return gYear for invalid values") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gYear")
    val maybeParsedDateTime = dateFormatObj.parse(
      "99" // the century must not be truncated
    )
    assert(maybeParsedDateTime.isLeft)
  }

  test("it should parse gYearMonth") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gYearMonth")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2004-04-05:00"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getYear == 2004)
    assert(parsedDateTime.getMonthValue == 4)
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
  }

  test("it should not return gYearMonth for invalid values") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#gYearMonth")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2004-4" //the month must be two digits
    )
    assert(maybeParsedDateTime.isLeft)
  }

  test("it should parse time") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#time")
    val maybeParsedDateTime = dateFormatObj.parse(
      "13:20:30.5555-05:00"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
    assert(parsedDateTime.getNano == 0.5555 * 1e9)
    assert(parsedDateTime.getHour == 13)
    assert(parsedDateTime.getMinute == 20)
    assert(parsedDateTime.getSecond == 30)
  }

  test("it should not return time for invalid values") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#time")
    val maybeParsedDateTime = dateFormatObj.parse(
      "5:20:00" // hours, minutes, and seconds must be two digits each
    )
    assert(maybeParsedDateTime.isLeft)
  }

  test("it should not return dateTimeStamp for invalid values") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#dateTimeStamp")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2004-04-12T13:20:00"
    )
    assert(maybeParsedDateTime.isLeft)
  }

  test("it should parse dateTimeStamp") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#dateTimeStamp")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2004-04-12T13:20:00-05:00"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)

  }

  test("it should parse date") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#date")
    val maybeParsedDateTime = dateFormatObj.parse(
      "-0045-01-01"
    )
    assert(maybeParsedDateTime.isRight)
    val Right(parsedDateTime) = maybeParsedDateTime
    assert(parsedDateTime.getDayOfMonth == 1)
    assert(parsedDateTime.getMonthValue == 1)
    assert(parsedDateTime.getYear == -45)
  }

  test("it should not retrieve date for invalid value") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#date")
    val maybeParsedDateTime = dateFormatObj.parse(
      "04-12-2004" //the value must be in CCYY-MM-DD order
    )
    assert(maybeParsedDateTime.isLeft)
  }
}
