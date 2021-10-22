package CSVValidation
import com.ibm.icu.math
import org.scalatest.FunSuite

class NumberFormatTest extends FunSuite {
  test("it formats the number received to the specified format (zero padding)") {
    val numberFormatObj = NumberFormat(Some("00000.0000"))
    assert(numberFormatObj.format(21.334) === "00021.3340")
  }

  test("it should recognize 1E6 as 1 million") {
    val numberFormatObj = NumberFormat(None)
    assert(numberFormatObj.parse("1E6") === 1000000)
  }

  test("scientific notion test 1") {
    val numberFormatObj = NumberFormat(Some("0.###E0"))
    assert(numberFormatObj.parse("1.234E3") === 1234)
  }

  test("scientific notion test 2") {
    val numberFormatObj = NumberFormat(Some("00.###E0"))
    val expectedOutput = 0.00123
    val receivedOutput = numberFormatObj.parse("12.3E-4")
    assert(receivedOutput.doubleValue() === expectedOutput)
  }

  test("scientific notion test 3") {
    val numberFormatObj = NumberFormat(Some("##0.####E0"))
    assert(numberFormatObj.parse("12.345E3") === 12345)
  }

  test("scientific notion test 4") {
    val numberFormatObj = NumberFormat(Some("##0.##E0"))
    assert(numberFormatObj.format(12345) === "12.3E3")
  }

  test("padding test 1") {
    val numberFormatObj = NumberFormat(Some("$*x#,##0.00"))
    assert(numberFormatObj.parse("$xx123.00") === 123)
  }

  test("padding using format test") {
    val numberFormatObj = NumberFormat(Some("$*x#,##0.00"))
    assert(numberFormatObj.parse("$xx123.00") === 123)
  }

  test("significant digits test 1") {
    val numberFormatObj = NumberFormat(Some("@##"))
    assert(numberFormatObj.format(0.1203) === "0.12")
  }

  test("should parse numbers that match %000 correctly") {
    val numberFormatObj = NumberFormat(Some("%000"))
    assert(numberFormatObj.parse("%001").doubleValue() === 0.01)
    assert(numberFormatObj.parse("%123").doubleValue() === 1.23)
    assert(numberFormatObj.parse("%1234").doubleValue() === 12.34)
  }

//  test("should parse negative numbers pattern correctly") {
//    val numberFormatObj = NumberFormat(Some("-0"))
//    assert(numberFormatObj.parse("1").doubleValue() === 1)
//  }
}