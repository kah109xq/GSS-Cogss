package CSVValidation

import org.scalatest.FunSuite

class LdmlNumberFormatParserTest extends FunSuite {

  test("Parsing a basic number works") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.#E0")
    val actual = parser.parse("3.4E2")
    assert(actual == Right(340.0), actual)
  }

  test("Parsing a number missing exponent fails") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.#E0")
    val actual = parser.parse("3.4")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("'E' expected but end of source found"), err)
  }

  test("Parsing a number not meeting minimum decimal padding fails") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("00.0")
    val actual = parser.parse("3.4")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("Expected a minimum of 2 integer digits."), err)
  }

  test("Parsing a number meeting minimum integer padding succeeds") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("00.0#")
    val actual = parser.parse("13.45")
    assert(actual == Right(BigDecimal("13.45")), actual)
  }

  test("Parsing a number not meeting minimum fractional padding fails") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.00")
    val actual = parser.parse("3.4")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("Expected a minimum of 2 fractional digits."), err)
  }

  test("Parsing a number meeting minimum fractional padding succeeds") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.00")
    val actual = parser.parse("3.41")
    assert(actual == Right(BigDecimal("3.41")), actual)
  }

  test("Parsing a number exceeding maximum fractional padding fails") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.00")
    val actual = parser.parse("3.412")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("Expected a maximum of 2 fractional digits."), err)
  }

  test("Quoted information is supported") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("3.41' some quoted message'")
    assert(actual == Right(BigDecimal("3.41")), actual)
  }

  test("Unterminated quoted leads to exception") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("'this has no terminating quote")
    }
    assert(thrown.getMessage == "Unterminated quote.", thrown.getMessage)
  }

  test("Escaped quote char is supported") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat(
        "'this is a message with an ''escaped'' quotation mark'0.00"
      )
    val actual = parser.parse(
      "'this is a message with an ''escaped'' quotation mark'3.41"
    )
    assert(actual == Right(BigDecimal("3.41")), actual)
  }

  test("Primary group char is supported in the integer part") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("3,123.1")
    assert(actual == Right(BigDecimal("3123.1")), actual)
  }

  test(
    "Number formatter supports custom grouping and decimal separator characters."
  ) {
    val frenchNumberFormatParser =
      LdmlNumberFormatParser(groupChar = '.', decimalChar = ',')
    val parser = frenchNumberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("3.123,1")
    assert(actual == Right(BigDecimal("3123.1")), actual)
  }

  test("Primary grouping - primary group size must be correct") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("31,23.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Secondary grouping - is supported in the integer part") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,34,56,789.1")
    assert(actual == Right(BigDecimal("123456789.1")), actual)
  }

  test("Secondary grouping - incorrect secondary group size returns error") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,345,56,789.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Secondary grouping - incorrect primary group size returns error") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,345,56,78.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Integer grouping sizes only apply to integer part of number") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.######")
    // intentionally contains group char in fractional part
    val actual = parser.parse("3,123.456,789")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Fractional grouping - primary grouping size works") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,##,##")
    val actual = parser.parse("3123.45,67,89")
    assert(actual == Right(BigDecimal("3123.456789")), actual)
  }

  test("Fractional grouping - primary and secondary grouping sizes work") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,###,###,##")
    val actual = parser.parse("123.45,678,901,23")
    assert(actual == Right(BigDecimal("123.4567890123")), actual)
  }

  test("Fractional grouping sizes only apply to fractional part of number") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,##,##")
    // intentionally contains group char in integer part
    val actual = parser.parse("3,123.45,67,89")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Only the first sign character should be used") {
    // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
    // > If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
    // > the first found should be used.
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("+-0.0")
    val actual = parser.parse("+-25.6")
    assert(actual == Right(BigDecimal("25.6")), actual)
  }

  test("Only the first exponent should be used") {
    // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
    // > If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
    // > the first found should be used.
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.0E0E0")
    val actual = parser.parse("2.4E1E2")
    assert(actual == Right(24))
  }

  test("E is not treated as an exponent if there is no following digit") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.0Explained")
    val actual = parser.parse("2.4Explained")
    assert(actual == Right(2.4))
  }
  test("E is not treated as an exponent if there is no preceding digit") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("ExplainThis:0.0")
    val actual = parser.parse("ExplainThis:1.5")
    assert(actual == Right(1.5))
  }

  test("Non-special character text is permitted (including white-space)") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("What do you mean? 0.0")
    val actual = parser.parse("What do you mean? 9.8")
    assert(actual == Right(9.8))
  }

  test("Percent sign can be included in format") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0%")
    val actual = parser.parse("25%")
    assert(actual == Right(25))
  }

  test("Per-mille sign can be included in format") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0‰")
    val actual = parser.parse("250‰")
    assert(actual == Right(250))
  }

  test("Rounding not supported") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("##0.1")
    }
    assert(
      thrown.getMessage == "Found rounding character '1'. Rounding functionality not implemented."
    )
  }

  test("Parser Throws an Exception where no Digit Chars are Found in Format") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat(
        "This does not contain any digit chars."
      )
    }
    assert(
      thrown.getMessage == "Number format does not contain any digits characters."
    )
  }

  test("Significant Figures Digits not supported") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("@@")
    }
    assert(
      thrown.getMessage == "Found significant figures character '@'. Significant figures functionality not implemented."
    )
  }
}
