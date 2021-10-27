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

  test("it should recognize NAN (Not A Number)") {
    val numberFormatObj = NumberFormat(None)
    assert(numberFormatObj.parse("NaN").doubleValue().isNaN)
  }

  test("it should recognize INF") {
    val numberFormatObj = NumberFormat(None)
    assert(numberFormatObj.parse("INF").doubleValue().isInfinity)
  }

  test("it should recognize negative infinity") {
    val numberFormatObj = NumberFormat(None)
    assert(numberFormatObj.parse("-INF").doubleValue().isNegInfinity)
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

  test("should parse numbers that contain ‰ (per-mille) correctly") {
    // This document says even if the pattern is not provided, implementations should recognise and parse numbers that
    // consist of ‰ which is not true for NumberFormat class. Check this if some problems occur in future.
    // https://www.w3.org/TR/2015/REC-tabular-data-model-20151217/#formats-for-numeric-types
    val numberFormatObj = NumberFormat(Some("‰000"))
    assert(numberFormatObj.parse("‰1000") === 1)
  }

  test("should correctly parse numbers in non-english-convention when Group and Decimal chars are configured to that convention") {
    // English convention: 5,246.30
    // Non-English convention: 5.246,30
    // https://en.wikipedia.org/wiki/Decimal_separator#Hindu-Arabic_numerals
    val numberFormatObjWithConfiguration = NumberFormat(Some("#,##0.00"), Some('.'), Some(','))
    assert(numberFormatObjWithConfiguration.parse("36.756,32").doubleValue() === 36756.32)
  }

  test("should not parse numbers in non-english-convention when Group and Decimal chars are standard") {
    val numberFormatObjWithoutConfiguration = NumberFormat(Some("#,##0.00"), None, None)
    assert(numberFormatObjWithoutConfiguration.parse("36.756,32").doubleValue() != 36756.32)
  }

  test("should correctly format numbers in non-english-convention when Group and Decimal chars are configured to that convention") {
    val numberFormatObjWithConfiguration = NumberFormat(Some("#,##0.00"), Some('.'), Some(','))
    assert(numberFormatObjWithConfiguration.format(36756.32) === "36.756,32")
  }

  test("should parse numbers when pattern is not provided") {
    val numberFormatObj = NumberFormat(None)
    assert(numberFormatObj.parse("3456.12345").doubleValue() === 3456.12345)
  }

  test("should format numbers when pattern is not provided") {
    val numberFormatObj = NumberFormat(None)
    assert(numberFormatObj.format(3456.12345) === "3,456.123")
  }

}