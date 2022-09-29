package CSVValidation

import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.util.parsing.combinator.RegexParsers
import CSVValidation.traits.LoggerExtensions.LogDebugException
import CSVValidation.traits.NumberParser
import models.ArrayCursor

import scala.collection.mutable.ArrayBuffer

case class LdmlNumberFormatParser(
    groupChar: Char = ',',
    decimalChar: Char = '.'
) extends RegexParsers {

  /**
    * Don't automatically consume/skip any whitespace characters.
    */
  override val whiteSpace = "".r

  val logger = Logger(this.getClass.getName)

  def numericDigitChars =
    Set('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '#', '@', ',')
  def plusSignChars = Set('+', '\uFF0B', '\u2795')
  def minusSignChars = Set('-')
  def signChars = plusSignChars.union(minusSignChars)

  // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
  def quoteCharsRegEx = "[\u0027\u2018\u02BB\u02BC\u2019\u05F3]".r
  def signCharsRegEx = s"[${signChars.mkString}]".r

  type NumberPartParser = Parser[Option[ParsedNumberPart]]

  /**
    * "Each subpattern has a prefix, a numeric part, and a suffix."
    *  https://www.unicode.org/reports/tr35/tr35-31/tr35-numbers.html#Number_Format_Patterns
    *
    * @param prefixParsers
    * @param numericPartParser
    * @param suffixParsers
    */
  case class LdmlNumericSubPattern(
      prefixParsers: Array[NumberPartParser],
      numericPartParsers: Array[NumberPartParser],
      suffixParsers: Array[NumberPartParser]
  ) {

    /**
      * Accumulate all of the existing parser up together into a parser which returns a list of results.
      * @return
      */
    def toParser(): Parser[Array[ParsedNumberPart]] = {
      val Array(firstParser, remainingParsers @ _*) =
        Array.concat(prefixParsers, numericPartParsers, suffixParsers)

      type ParserAccumulator = Parser[ArrayBuffer[ParsedNumberPart]]

      remainingParsers.foldLeft[ParserAccumulator](
        firstParser ^^ (_.map(ArrayBuffer(_)).getOrElse(ArrayBuffer.empty))
      )((accumulatedParsers, nextNumberPartParser) =>
        accumulatedParsers ~ nextNumberPartParser ^^ {
          case accumulatedNumberParts ~ Some(nextNumberPart) =>
            accumulatedNumberParts.append(nextNumberPart)
            accumulatedNumberParts
          case accumulatedNumberParts ~ None => accumulatedNumberParts
        }
      ) ^^ (accumulatedNumberParts => accumulatedNumberParts.toArray)
    }
  }

  def parseQuote(
      format: ArrayCursor[Char]
  ): Either[String, Parser[Option[ParsedNumberPart]]] = {
    val quotedText = new mutable.StringBuilder()
    var quoteEnded = false
    while (format.hasNext() && !quoteEnded) {
      format.next() match {
        case '\'' if format.hasNext() && format.peekNext() == '\'' =>
          // Escaped quotation mark `''` (two single quotes)
          format.next()
          quotedText.append("''")
        case '\'' => quoteEnded = true
        case c    => quotedText.append(c)
      }
    }

    if (quoteEnded)
      Right(
        quoteCharsRegEx ~> quotedText
          .toString() <~ quoteCharsRegEx ^^^ None
      )
    else
      Left("Closing quotation mark missing.")
  }

  def parseNumberWithPossibleExponent(
      format: ArrayCursor[Char]
  ): Array[Parser[Option[ParsedNumberPart]]] = {
    var numberEnded = false
    var parserParts: Array[Parser[Option[ParsedNumberPart]]] = Array(
      // Parse an (optional) sign at the beginning of the number.
      // Yes, the format may specify a different position for the sign char but we only count the *first* one we come
      // across which should enable sensible behaviour.
      opt(
        s"[${plusSignChars.mkString}]".r ^^^ SignPart(true) |
          s"[${minusSignChars.mkString}]".r ^^^ SignPart(false)
      )
    )
    var isFractionalPart = false
    while (format.hasNext() && !numberEnded) {
      val nextChar = format.next()
      nextChar match {
        case '.' =>
          isFractionalPart = true
          parserParts :+= opt(decimalChar.toString) ^^^ None
        case c if numericDigitChars.contains(c) =>
          format.stepBack()
          parserParts :+= parseDigits(format, isFractionalPart)
        // `E` should only be recognised as the exponent char if it is preceded and followed by digits.
        // http://www.unicode.org/reports/tr35/tr35-numbers.html#sci
        case 'E' if surroundingCharsMatchExponentPattern(format) =>
          parserParts :+= extractExponentPartParser(format)
        case _ =>
          numberEnded = true
          // Step back one position in the format and let the parser continue from there.
          format.stepBack()
      }
    }

    parserParts
  }

  private def extractExponentPartParser(
      format: ArrayCursor[Char]
  ): Parser[Some[ExponentPart]] = {
    val signPartParser: Parser[SignPart] =
      s"[${plusSignChars.mkString}]".r ^^^ SignPart(true) |
        s"[${minusSignChars.mkString}]".r ^^^ SignPart(false)

    val exponentWithSignParser: Parser[SignPart] = {
      if (signChars.contains(format.peekNext())) {
        format.next()
        // Format requires a sign to be present.
        "E" ~ (signPartParser | failure(
          "Expected explicit sign character [+-] missing"
        )) ^^ { case _ ~ s => s }
      } else {
        "E" ~ opt(signPartParser) ^^ {
          // Sign is optional and positive by default
          case _ ~ s => s.getOrElse(SignPart(true))
        }
      }
    }

    val exponentDigits = parseDigits(format, false) ^^ (_.get)

    exponentWithSignParser ~ exponentDigits ^^ {
      case sign ~ digits =>
        Some(
          ExponentPart(
            sign.isPositive,
            digits
          )
        )
    }
  }

  private def surroundingCharsMatchExponentPattern(
      positionInFormat: ArrayCursor[Char]
  ): Boolean = {
    /*
      "The exponent character can only be interpreted as such if it occurs after at least one digit, and if it is
       followed by at least one digit, with only an optional sign in between. A regular expression may be helpful here."
      https://www.unicode.org/reports/tr35/tr35-31/tr35-numbers.html#Parsing_Numbers
     */
    if (!(positionInFormat.hasPrevious() && positionInFormat.hasNext()))
      return false

    val previousIsDigit =
      numericDigitChars.contains(positionInFormat.peekPrevious())

    if (!previousIsDigit)
      return false

    val nextIsDigit =
      numericDigitChars.contains(positionInFormat.peekNext())

    if (nextIsDigit)
      return true
    /*
      "To prefix positive exponents with a localized plus sign, specify '+' between the exponent and the digits:
       "0.###E+0" will produce formats "1E+1", "1E+0", "1E-1", and so on."
      https://www.unicode.org/reports/tr35/tr35-31/tr35-numbers.html#Number_Format_Patterns
     */
    val nextIsSignChar = signChars.contains(positionInFormat.peekNext())
    val followingIsDigit = positionInFormat.hasValue(2) &&
      numericDigitChars.contains(positionInFormat.peek(2))

    nextIsSignChar && followingIsDigit
  }

  def parseDigits(
      format: ArrayCursor[Char],
      isFractionalPart: Boolean
  ): Parser[Some[DigitsPart]] = {
    var finished = false

    var nonPaddingDigits = 0
    var zeroPaddingDigits = 0

    var groupsPresent = false
    var currentGroupSize: Int = 0
    val groupSizes = mutable.ArrayBuffer[Int]()

    while (format.hasNext() && !finished) {
      format.next() match {
        case '0' => {
          if (nonPaddingDigits > 0 && isFractionalPart) {
            throw NumberFormatError(
              "Zero-padding digits defined after optional digits in fractional part."
            )
          }
          zeroPaddingDigits += 1
          if (groupsPresent) currentGroupSize += 1
        }
        case '#' => {
          if (zeroPaddingDigits > 0 && !isFractionalPart) {
            throw NumberFormatError(
              "Optional digits defined after zero-padding digits in integer part."
            )
          }
          nonPaddingDigits += 1
          if (groupsPresent) currentGroupSize += 1
        }
        case char @ '@' =>
          throw NumberFormatError(
            s"Found significant figures character '$char'. Significant figures functionality not implemented."
          )
        case char @ ('1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9') =>
          throw NumberFormatError(
            s"Found rounding character '$char'. Rounding functionality not implemented."
          )
        case ',' =>
          if (groupsPresent) {
            groupSizes.append(currentGroupSize)
            currentGroupSize = 0
          } else {
            groupsPresent = true
            if (isFractionalPart) {
              // We need to start counting fractional group sizes before we hit the first groupChar.
              groupSizes.append(zeroPaddingDigits + nonPaddingDigits)
            }
          }
        case _ =>
          finished = true
          // Step back one position in the format and let the parser continue from there.
          format.stepBack()
      }
    }
    if (groupsPresent && !isFractionalPart) {
      // When counting decimal group sizes, we need to count the last group before the decimal dot, even though
      // it won't finish with a groupChar.
      groupSizes.append(currentGroupSize)
    }
    currentGroupSize = 0

    val firstGroupSize =
      if (groupSizes.isEmpty) None else Some(groupSizes(0))
    val secondGroupSize =
      if (groupSizes.length <= 1) None else Some(groupSizes(1))

    val digitMatcherRegex = if (groupsPresent) s"[0-9$groupChar]" else s"[0-9]"

    getParserForDigitsInGroupings(
      isFractionalPart,
      zeroPaddingDigits,
      nonPaddingDigits + zeroPaddingDigits,
      firstGroupSize,
      secondGroupSize,
      digitMatcherRegex
    ) ^^ (digits =>
      if (isFractionalPart)
        Some(FractionalDigitsPart(digits))
      else
        Some(IntegerDigitsPart(digits))
    )
  }

  private def getParserForDigitsInGroupings(
      isFractionalPart: Boolean,
      minimumDigits: Int,
      maximumDigits: Int,
      firstGroupSize: Option[Int],
      secondGroupSize: Option[Int],
      digitAndGroupingMatcherRegex: String
  ): Parser[String] = {
    val numberPartDescription =
      if (isFractionalPart) "fractional" else "integer"

    val greedyDigitAndGroupingMatcher: Parser[String] =
      s"$digitAndGroupingMatcherRegex{$minimumDigits,}".r | failure(
        s"Expected a minimum of $minimumDigits $numberPartDescription digits."
      )

    def ensureCorrectNumDigits(
        input: String
    ): String = {
      (isFractionalPart, input.length) match {
        case (_, l) if l < minimumDigits =>
          throw NumberFormatError(
            s"Expected a minimum of $minimumDigits $numberPartDescription digits."
          )

        /*
        "The number of # placeholder characters before the decimal do not matter, since no limit is placed on the maximum number of digits"
        https://www.unicode.org/reports/tr35/tr35-31/tr35-numbers.html#Number_Patterns
         */
        case (true, l) if l > maximumDigits =>
          throw NumberFormatError(
            s"Expected a maximum of $maximumDigits $numberPartDescription digits."
          )
        case _ => input
      }

    }

    Parser { p =>
      greedyDigitAndGroupingMatcher(p) match {
        case Success(digitAndGroupChars, currentPosition) =>
          try {
            parseAll(
              enforceGroupsRemoveGroupCharsParser(
                firstGroupSize,
                secondGroupSize,
                isFractionalPart
              ) ^^ ensureCorrectNumDigits,
              digitAndGroupChars
            ) match {
              case Success(digitsWithoutGroupingChar, _) =>
                Success(digitsWithoutGroupingChar, currentPosition)
              case Failure(err, _) => Failure(err, currentPosition)
            }
          } catch {
            case e: Throwable =>
              logger.debug(e)
              Failure(e.getMessage, currentPosition)
          }
        case failure @ Failure(_, _) => failure
      }
    }
  }

  /**
    * Enforces that the primary/secondary group characters must be correctly used within a number.
    *
    * @param firstGroupSize
    * @param secondGroupSize
    * @return
    */
  def enforceGroupsRemoveGroupCharsParser(
      firstGroupSize: Option[Int],
      secondGroupSize: Option[Int],
      isInFractionalPart: Boolean
  ): Parser[String] = {
    val groupingParser: Option[Parser[String]] =
      (firstGroupSize, secondGroupSize) match {
        case (Some(firstSize), Some(secondSize)) =>
          val primaryGroupSize =
            if (isInFractionalPart) firstSize else secondSize
          val secondaryGroupSize =
            if (isInFractionalPart) secondSize else firstSize
          Some(
            getGroupsParserForPrimarySecondaryGrouping(
              primaryGroupSize,
              secondaryGroupSize,
              isInFractionalPart
            )
          )
        case (Some(primaryGroupSize), None) =>
          Some(
            getGroupsParserForPrimaryGrouping(
              primaryGroupSize,
              isInFractionalPart
            )
          )
        case (_, _) => None
      }

    groupingParser.getOrElse("[0-9]*".r)
  }

  private def getGroupsParserForPrimarySecondaryGrouping(
      primaryGroupSize: Int,
      secondaryGroupSize: Int,
      isInFractionalPart: Boolean
  ): Parser[String] = {
    val groupsParser = if (isInFractionalPart) {
      // Fractional part
      // Given a primary group size of 3 and a secondary grouping size of 2, this matches things like:
      // 0.000,(00,)*(0)?
      // 0.###,(##,)*(#)?
      s"[0-9]{$primaryGroupSize}".r ~
        rep(groupChar ~> s"[0-9]{$secondaryGroupSize}".r) ~
        opt(groupChar ~> s"[0-9]{1,$secondaryGroupSize}".r) ^^ {
        case primaryGroup ~ secondaryGroups ~ last =>
          (
            List(primaryGroup)
              ++ secondaryGroups
              :+ last.getOrElse("")
          ).mkString("")
      }
    } else {
      // Integer part
      // Given a primary group size of 3 and a secondary grouping size of 2, this matches things like:
      // (0,)?(00,)*,000.0
      // (#,)?(##,)*,###.0
      opt(s"[0-9]{1,$secondaryGroupSize}".r <~ groupChar) ~
        rep(s"[0-9]{$secondaryGroupSize}".r <~ groupChar) ~
        s"[0-9]{$primaryGroupSize}".r ^^ {
        case first ~ secondaryGroups ~ primaryGroup =>
          (
            List(first.getOrElse(""))
              ++ secondaryGroups
              :+ primaryGroup
          ).mkString("")
      }

    }

    // Support numbers which don't have enough digits to show a single group.
    groupsParser | s"[0-9]{0,$primaryGroupSize}".r
  }

  private def getGroupsParserForPrimaryGrouping(
      primaryGroupSize: Int,
      isInFractionalPart: Boolean
  ): Parser[String] = {
    val primaryGroupMatch = s"[0-9]{$primaryGroupSize}".r
    val partialGroupMatch = s"[0-9]{1,$primaryGroupSize}".r

    val groupsParser = if (isInFractionalPart) {
      // Fractional part
      // Given a primary group size of 3, this matches things like:
      // 0.000,(000,)*(00)?
      // #.###,(###,)*(##)?
      primaryGroupMatch ~
        rep(groupChar ~> primaryGroupMatch) ~
        opt(groupChar ~> partialGroupMatch) ^^ {
        case first ~ groups ~ last =>
          (
            List(first)
              ++ groups
              :+ last.getOrElse("")
          ).mkString("")
      }
    } else {
      // Integer part.
      // Given a primary group size of 3, this matches things like:
      // (00,)?(000,)*(000).0
      // (##,)?(###,)*(###).#
      opt(partialGroupMatch <~ groupChar) ~
        rep(primaryGroupMatch <~ groupChar) ~
        primaryGroupMatch ^^ {
        case first ~ groups ~ last =>
          (
            List(first.getOrElse(""))
              ++ groups
              :+ last
          ).mkString("")
      }
    }

    // Support numbers which don't have enough digits to show a single group.
    groupsParser | s"[0-9]{0,$primaryGroupSize}".r
  }

  case class LdmlNumericParserForFormat(private val parser: Parser[BigDecimal])
      extends NumberParser {
    def parse(number: String): Either[String, BigDecimal] = {
      parseAll(parser, number) match {
        case Success(result, _)      => Right(result)
        case failure @ Failure(_, _) => Left(failure.toString())
        case error @ Error(_, _)     => Left(error.toString())
      }
    }
  }

  def getParserForFormat(format: String): LdmlNumericParserForFormat =
    LdmlNumericParserForFormat(getParser(format))

  private def getParser(
      formatIn: String
  ): Parser[BigDecimal] = {
    val format = ArrayCursor[Char](formatIn.toCharArray)
    var subPatterns = Array.empty[LdmlNumericSubPattern]
    while (format.hasNext()) {
      subPatterns :+= extractSubPatternParsers(format)
    }

    val (
      positivePattern: LdmlNumericSubPattern,
      negativePattern: Option[LdmlNumericSubPattern]
    ) = subPatterns match {
      case Array()         => throw NumberFormatError("No pattern provided.")
      case Array(pos)      => (pos, None)
      case Array(pos, neg) => (pos, Some(neg))
      case _ =>
        throw NumberFormatError(
          s"Found ${subPatterns.length} sub-patterns. Expected at most two (positive;negative)."
        )
    }

    getParserForSubPatterns(positivePattern, negativePattern)
  }

  def extractSubPatternParsers(
      format: ArrayCursor[Char]
  ): LdmlNumericSubPattern = {
    val prefixParsers = ArrayBuffer.empty[NumberPartParser]
    val numericPartParsers = ArrayBuffer.empty[NumberPartParser]
    val suffixParsers = ArrayBuffer.empty[NumberPartParser]

    // Start accumulating in the prefixes array, switch to suffix once the numeric part has been parsed
    var parsers = prefixParsers

    var subPatternComplete: Boolean = false
    while (format.hasNext() && !subPatternComplete) {
      format.next() match {
        case '\'' =>
          parseQuote(format) match {
            case Right(quoteParser) => parsers.append(quoteParser)
            case Left(err)          => throw NumberFormatError(err)
          }
        case c if numericDigitChars.contains(c) =>
          format.stepBack()
          numericPartParsers.addAll(parseNumberWithPossibleExponent(format))
          // We've done the number parsing part, the rest of the pattern is the suffix
          parsers = suffixParsers
        case char if signChars.contains(char) =>
          /*
           We assume that if the user specifies '+' in the pattern, they are *not* asserting that all numbers matching
           the format should be positive; this would be overly restrictive. Instead they are saying a '+' or '-' char
           must be present in the pattern.

           "An explicit "plus" format can be formed, so as to show a visible + sign when formatting a non-negative
            number."
           http://www.unicode.org/reports/tr35/tr35-numbers.html#Explicit_Plus
           */
          parsers.append(
            signCharsRegEx ^^ {
              case "+" => Some(SignPart(true))
              case "-" => Some(SignPart(false))
            } | failure("Expected explicit sign character [+-] missing")
          )
        case char @ '%' =>
          parsers.append(
            char.toString ^^^ Some(PercentagePart())
          )
        case char @ '‰' =>
          parsers.append(
            char.toString ^^^ Some(PerMillePart())
          )
        case char @ '¤' =>
          parsers.append(
            char.toString ^^^ None
          )
        case ';' => subPatternComplete = true
        // todo: Need to deal with special padding chars
        case char =>
          // http://www.unicode.org/reports/tr35/tr35-numbers.html#Special_Pattern_Characters
          // "Many characters in a pattern are taken literally; they are matched during parsing and output
          //  unchanged during formatting"
          parsers.append(
            char.toString ^^^ None // Be permissive.
          )
      }
    }

    if (numericPartParsers.isEmpty) {
      throw NumberFormatError(
        "Number format does not contain any digit characters."
      )
    }

    LdmlNumericSubPattern(
      prefixParsers.toArray,
      numericPartParsers.toArray,
      suffixParsers.toArray
    )
  }

  private def getParserForSubPatterns(
      positivePattern: LdmlNumericSubPattern,
      negativePattern: Option[LdmlNumericSubPattern]
  ): Parser[BigDecimal] = {
    negativePattern
      .map(negative => {
        val finalNegativePattern = negative
          .copy(
            /*
              "If there is an explicit negative subpattern, it serves only to specify the negative prefix and suffix;
               the number of digits, minimal digits, and other characteristics are ignored in the negative subpattern.
               That means that "#,##0.0#;(#)" has precisely the same result as "#,##0.0#;(#,##0.0#)"."
              https://www.unicode.org/reports/tr35/tr35-31/tr35-numbers.html#Number_Format_Patterns
             */
            numericPartParsers = positivePattern.numericPartParsers,
            // Add a SignPart to make sure the number is treated as negative.
            prefixParsers =
              ("" ^^^ Some(SignPart(false))) +: negative.prefixParsers
          )

        (
          positivePattern.toParser() |
            finalNegativePattern.toParser()
        ) ^^ mapNumberPartsToParsedNumber
      })
      .getOrElse(positivePattern.toParser() ^^ mapNumberPartsToParsedNumber)
      .map(parsedNumber => parsedNumber.toBigDecimal())
  }

  private def mapNumberPartsToParsedNumber(
      numberParts: Array[ParsedNumberPart]
  ): ParsedNumber = {
    val parsedNumber: ParsedNumber = ParsedNumber()
    for (numberPart <- numberParts) {
      // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
      // "If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
      //  the first found should be used."
      numberPart match {
        case s @ SignPart(_) =>
          if (parsedNumber.sign.isEmpty) parsedNumber.sign = Some(s)
        case i @ IntegerDigitsPart(_) => parsedNumber.integerDigits = Some(i)
        case f @ FractionalDigitsPart(_) =>
          parsedNumber.fractionalDigits = Some(f)
        case e @ ExponentPart(_, _) =>
          if (parsedNumber.exponent.isEmpty)
            parsedNumber.exponent = Some(e)
        case factor: ScalingFactorPart => // e.g. per-mille & percent
          if (parsedNumber.scalingFactor.isEmpty)
            parsedNumber.scalingFactor = Some(factor)
      }
    }
    parsedNumber
  }
}
