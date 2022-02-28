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
    val dateFormatObj =
      DateFormat(None, Some("http://www.w3.org/2001/XMLSchema#dateTime"))
    assert(
      dateFormatObj.parse(
        "2002-10-10T12:00:00.012-05:00"
      ) === "#dateTime"
    )
  }

}
