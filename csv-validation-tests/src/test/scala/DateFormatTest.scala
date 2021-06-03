import org.scalatest.FunSuite
import org.joda.time._

class DateFormatTest extends FunSuite {
  test("it should parse dates that match yyyy-MM-dd correctly") {
    val df = DateFormat("yyyy-MM-dd", "")
    val date = new LocalDate("2015-03-22")
    assert(df.parse("2015-03-22")("dateTime") === date)
  }
}