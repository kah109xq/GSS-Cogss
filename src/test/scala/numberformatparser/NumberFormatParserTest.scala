package CSVValidation

import org.scalatest.FunSuite

class NumberFormatParserTest extends FunSuite {

  test("Parsing a basic number works") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.#E0")
    val actual = parser.parse("3.4E2")
    assert(actual == Right(340.0))
  }

  test("Parsing a number missing exponent fails") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.#E0")
    val actual = parser.parse("3.42")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("'E' expected but end of source found"))
  }

  test("Parsing a number not meeting minimum decimal padding fails") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("00.0")
    val actual = parser.parse("3.4")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("Expected at least 2 integer digits."))
  }

  test("Parsing a number meeting minimum integer padding succeeds") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("00.0")
    val actual = parser.parse("13.45")
    assert(actual == Right(BigDecimal("13.45")))
  }

  test("Parsing a number not meeting minimum fractional padding fails") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.00")
    val actual = parser.parse("3.4")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("Expected at least 2 fractional digits."))
  }

  test("Parsing a number meeting minimum fractional padding succeeds") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.00")
    val actual = parser.parse("3.401")
    assert(actual == Right(BigDecimal("3.401")))
  }

  test("Quoted information is supported") {
    val numberFormatParser = NumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("3.401' some quoted message'")
    assert(actual == Right(BigDecimal("3.401")))
  }

  test("Unterminated quoted leads to exception") {
    val numberFormatParser = NumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("'this has no terminating quote")
    }
    assert(thrown.getMessage == "Unterminated quote.")
  }

  test("Escaped quote char is supported") {
    val numberFormatParser = NumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat(
        "'this is a message with an ''escaped'' quotation mark'0.00"
      )
    val actual = parser.parse(
      "'this is a message with an ''escaped'' quotation mark'3.401"
    )
    assert(actual == Right(BigDecimal("3.401")))
  }

  test("Primary group char is supported in the integer part") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("3,123.1")
    assert(actual == Right(BigDecimal("3123.1")))
  }

  test("Primary grouping - primary group size must be correct") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("31,23.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"))
  }

  test("Secondary grouping - is supported in the integer part") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,34,56,789.1")
    assert(actual == Right(BigDecimal("123456789.1")))
  }

  test("Secondary grouping - incorrect secondary group size returns error") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,345,56,789.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"))
  }

  test("Secondary grouping - incorrect primary group size returns error") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,345,56,78.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"))
  }

  test("Integer grouping sizes only apply to integer part of number") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    // intentionally contains group char in fractional part
    val actual = parser.parse("3,123.456,789")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"))
  }

  test("Fractional grouping - primary grouping size works") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,##")
    val actual = parser.parse("3123.45,67,89")
    assert(actual == Right(BigDecimal("3123.456789")))
  }

  test("Fractional grouping - primary and secondary grouping sizes work") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,###,#")
    val actual = parser.parse("123.45,678,901,23")
    assert(actual == Right(BigDecimal("123.4567890123")))
  }

  test("Fractional grouping sizes only apply to fractional part of number") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,##")
    // intentionally contains group char in integer part
    val actual = parser.parse("3,123.45,67,89")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("'.' expected but ',' found"))
  }

  test("Only the first sign character should be used") {
    // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
    // > If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
    // > the first found should be used.
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("+-0.0")
    // intentionally contains group char in integer part
    val actual = parser.parse("+-25.6")
    assert(actual == Right(BigDecimal("25.6")))
  }

  test("Only the first exponent should be used") {
    // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
    // > If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
    // > the first found should be used.
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.0E0E0")
    // intentionally contains group char in integer part
    val actual = parser.parse("2.4E1E2")
    assert(actual == Right(24))
  }

  test("Percent sign can be included in format") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0%")
    val actual = parser.parse("25%")
    assert(actual == Right(25))
  }

  test("Per-mille sign can be included in format") {
    val numberFormatParser = NumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0‰")
    // intentionally contains group char in integer part
    val actual = parser.parse("250‰")
    assert(actual == Right(250))
  }

  test("Rounding not supported") {
    val numberFormatParser = NumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("##0.1")
    }
    assert(
      thrown.getMessage == "Found rounding character '1'. Rounding functionality not implemented."
    )
  }

  test("Significant Figures Digits not supported") {
    val numberFormatParser = NumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("@@")
    }
    assert(
      thrown.getMessage == "Found significant figures character '@'. Significant figures functionality not implemented."
    )
  }
}
