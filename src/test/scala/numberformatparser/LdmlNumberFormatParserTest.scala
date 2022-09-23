package CSVValidation

import org.scalatest.FunSuite

class LdmlNumberFormatParserTest extends FunSuite {

  test("Parsing a Basic Number Works") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.#E0")
    val actual = parser.parse("3.4E2")
    assert(actual == Right(340.0), actual)
  }

  test("Parsing a Value with a Negative Sub-Pattern Succeeds") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.0#;(#)")
    val actualPositive = parser.parse("3.4")
    assert(actualPositive == Right(3.4), actualPositive)
    val actualNegative = parser.parse("(25.91)")
    assert(actualNegative == Right(-25.91), actualNegative)
    // todo: Fix this negative sub-pattern pattern. It just doesn't work at all.
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

  test("Support MODIFIER LETTER TURNED COMMA (ʻ) Quote Character") {
    // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("9.34ʻ some quoted messageʻ")
    assert(actual == Right(BigDecimal("9.34")), actual)
  }

  test("Support LEFT SINGLE QUOTATION MARK (‘) Quote Character") {
    // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("5.16‘ some quoted message‘")
    assert(actual == Right(BigDecimal("5.16")), actual)
  }

  test("Support MODIFIER LETTER APOSTROPHE (ʼ) Quote Character") {
    // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("1.58ʼ some quoted messageʼ")
    assert(actual == Right(BigDecimal("1.58")), actual)
  }

  test("Support RIGHT SINGLE QUOTATION MARK (’) Quote Character") {
    // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("6.55’ some quoted message’")
    assert(actual == Right(BigDecimal("6.55")), actual)
  }

  test("Support APOSTROPHE (') Quote Character") {
    // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("1.23' some quoted message'")
    assert(actual == Right(BigDecimal("1.23")), actual)
  }

  test("Support HEBREW PUNCTUATION GERESH (\u05F3) Quote Character") {
    // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
    val numberFormatParser = LdmlNumberFormatParser()
    val parser =
      numberFormatParser.getParserForFormat("0.00' some quoted message'")
    val actual = parser.parse("8.17\u05F3 some quoted message\u05F3")
    assert(actual == Right(BigDecimal("8.17")), actual)
  }

  test("Unterminated Quoted Section Should lead to an Exception") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("'this has no terminating quote")
    }
    assert(
      thrown.getMessage == "Closing quotation mark missing.",
      thrown.getMessage
    )
  }

  test("Escaped Quote Char is Supported") {
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

  test("Primary Grouping Char is Supported in the Integer Part") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("3,123.1")
    assert(actual == Right(BigDecimal("3123.1")), actual)
  }

  test(
    "Number Formatter Supports Custom Grouping and Decimal Separator Characters."
  ) {
    val frenchNumberFormatParser =
      LdmlNumberFormatParser(groupChar = '.', decimalChar = ',')
    val parser = frenchNumberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("3.123,1")
    assert(actual == Right(BigDecimal("3123.1")), actual)
  }

  test("Primary Grouping - Primary Group Size Must be Correct") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.##")
    val actual = parser.parse("31,23.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test(
    "Secondary Grouping - Parsing Number with Valid Secondary Grouping Succeeds"
  ) {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,34,56,789.1")
    assert(actual == Right(BigDecimal("123456789.1")), actual)
  }

  test(
    "Secondary Grouping - Parsing Number with Incorrect Secondary Group Size Returns Error"
  ) {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,345,56,789.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test(
    "Secondary grouping - Parsing Number with Incorrect Primary Group Size Returns Error"
  ) {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##,##0.##")
    val actual = parser.parse("12,345,56,78.1")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Integer Grouping Sizes Only Apply to the Integer Part of a Number") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#,##0.######")
    // intentionally contains group char in fractional part
    val actual = parser.parse("3,123.456,789")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Fractional Grouping - Parsing Number with Primary Grouping Succeeds") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,##,##")
    val actual = parser.parse("3123.45,67,89")
    assert(actual == Right(BigDecimal("3123.456789")), actual)
  }

  test(
    "Fractional Grouping - Parsing Number with Primary and Secondary Grouping Succeeds"
  ) {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,###,###,##")
    val actual = parser.parse("123.45,678,901,23")
    assert(actual == Right(BigDecimal("123.4567890123")), actual)
  }

  test("Fractional Grouping Sizes only Apply to Fractional Part of Number") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("#0.##,##,##")
    // intentionally contains group char in integer part
    val actual = parser.parse("3,123.45,67,89")
    assert(actual.isLeft)
    val Left(err) = actual
    assert(err.contains("end of input expected"), err)
  }

  test("Only the First Sign Character Should be Used") {
    // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
    // > If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
    // > the first found should be used.
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("+-0.0")
    val actual = parser.parse("+-25.6")
    assert(actual == Right(BigDecimal("25.6")), actual)
  }

  test("Only the First Exponent Should be Used") {
    // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
    // > If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
    // > the first found should be used.
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.0E0E0")
    val actual = parser.parse("2.4E1E2")
    assert(actual == Right(24))
  }

  test("'E' is not Treated as an Exponent if There is No Following Digit") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0.0Explained")
    val actual = parser.parse("2.4Explained")
    assert(actual == Right(2.4))
  }
  test("'E' is not Treated as an Exponent if There is no Preceding Digit") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("Explain This: 0.0")
    val actual = parser.parse("Explain This: 1.5")
    assert(actual == Right(1.5))
  }

  test("Non-special Character Text is Permitted (including white-space)") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("What do you mean? 0.0")
    val actual = parser.parse("What do you mean? 9.8")
    assert(actual == Right(9.8))
  }

  test("Percent Sign is Correctly Parsed") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0%")
    val actual = parser.parse("25%")
    assert(actual == Right(0.25))
  }

  test("Per-mille Sign is Correctly Parsed") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("0‰")
    val actual = parser.parse("130‰")
    assert(actual == Right(0.13))
  }

  test("Parsing Currency Symbols Works") {
    val numberFormatParser = LdmlNumberFormatParser()
    val parser = numberFormatParser.getParserForFormat("£#0.00")
    val actual = parser.parse("£2.50")
    assert(actual == Right(2.5))
  }

  test("Parser Throws an Exception where no Digit Chars are Found in Format") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat(
        "This does not contain any digit characters."
      )
    }
    assert(
      thrown.getMessage == "Number format does not contain any digit characters."
    )
  }

  test("Rounding not Supported") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("##0.1")
    }
    assert(
      thrown.getMessage == "Found rounding character '1'. Rounding functionality not implemented."
    )
  }

  test("Significant Figures Digits not Supported") {
    val numberFormatParser = LdmlNumberFormatParser()
    val thrown = intercept[NumberFormatError] {
      numberFormatParser.getParserForFormat("@@")
    }
    assert(
      thrown.getMessage == "Found significant figures character '@'. Significant figures functionality not implemented."
    )
  }
}
