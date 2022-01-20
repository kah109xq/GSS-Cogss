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

  test("it sets type as date when a date is provided") {
    val dateFormatObj = DateFormat(Some("yyyy-mm-dd"), None)
    assert(dateFormatObj.getType() === "http://www.w3.org/2001/XMLSchema#date")
  }

  test("it sets type as datetime when a datetime pattern is provided") {
    val dateFormatObj = DateFormat(Some("MM/dd/yyyy HH:mm"), None)
    assert(
      dateFormatObj.getType() === "http://www.w3.org/2001/XMLSchema#dateTime"
    )
  }

  test("it sets type as time when a time pattern is provided") {
    val dateFormatObj = DateFormat(Some("HH:mm:ss"), None)
    assert(dateFormatObj.getType() === "http://www.w3.org/2001/XMLSchema#time")
  }

  test("it sets the default format when format is not supplied") {
    val dateFormatObj =
      DateFormat(None, Some("http://www.w3.org/2001/XMLSchema#dateTime"))
    assert(
      dateFormatObj.getType() === "http://www.w3.org/2001/XMLSchema#dateTime"
    )
    assert(dateFormatObj.getFormat() === "YYYY-MM-DDThh:mm:ss.sTZD")
  }
}
