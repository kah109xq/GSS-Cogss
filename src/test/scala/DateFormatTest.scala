package CSVValidation
import org.scalatest.FunSuite

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

    assert(maybeParsedDateTime.isDefined)
    val parsedDateTime = maybeParsedDateTime.get
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

    assert(maybeParsedDateTime.isDefined)
    val parsedDateTime = maybeParsedDateTime.get
    assert(parsedDateTime.getYear == 2002)
    assert(parsedDateTime.getMonthValue == 10)
    assert(parsedDateTime.getDayOfMonth == 10)
    assert(parsedDateTime.getHour == 12)
    assert(parsedDateTime.getMinute == 0)
    assert(parsedDateTime.getSecond == 0)
    assert(parsedDateTime.getNano == 0.012 * 1e9)
    assert(parsedDateTime.getOffset.getTotalSeconds == -5 * 60 * 60)
  }

  test("it should parse datetime without timezone information") {
    val dateFormatObj =
      DateFormat(None, "http://www.w3.org/2001/XMLSchema#dateTime")
    val maybeParsedDateTime = dateFormatObj.parse(
      "2002-10-10T12:00:00.0123456"
    )

    assert(maybeParsedDateTime.isDefined)
    val parsedDateTime = maybeParsedDateTime.get
    assert(2002 == parsedDateTime.getYear)
    assert(10 == parsedDateTime.getMonthValue)
    assert(10 == parsedDateTime.getDayOfMonth)
    assert(12 == parsedDateTime.getHour)
    assert(0 == parsedDateTime.getMinute)
    assert(0 == parsedDateTime.getSecond)
    assert(0.012 * 1e9 == parsedDateTime.getNano)
  }

}
