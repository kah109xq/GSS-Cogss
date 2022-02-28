package CSVValidation
import org.scalatest.FunSuite

class DateFormatTest extends FunSuite {
  test(
    "it throws an exception for unrecognized date field symbols in date format"
  ) {
    val thrown = intercept[DateFormatError] {
      DateFormat(Some("yyyy-MM-ddUNKNOWN"), None)
    }
    assert(
      thrown.getMessage === "Unrecognised date field symbols in date format"
    )
  }

  test("it sets the default format when format is not supplied") {
    val dateFormatObj =
      DateFormat(None, Some("http://www.w3.org/2001/XMLSchema#dateTime"))
    assert(dateFormatObj.getFormat() === "YYYY-MM-DDThh:mm:ss.sTZD")
  }

  test("new test") {
    val dateFormatObj = DateFormat(None, Some("http://www.w3.org/2001/XMLSchema#dateTime"))
    val maybeParsedDateTime = dateFormatObj.parse(
      "2002-10-10T12:00:00.012-05:00"
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
    assert(-5 * 60 * 60 == parsedDateTime.getOffset.getTotalSeconds)
  }

}
