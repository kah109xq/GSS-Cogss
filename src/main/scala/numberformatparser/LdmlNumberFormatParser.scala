package CSVValidation

import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.util.parsing.combinator.RegexParsers
import CSVValidation.traits.LoggerExtensions.LogDebugException
import CSVValidation.traits.NumberParser
import models.ArrayCursor

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

  // https://www.unicode.org/reports/tr35/tr35.html#Loose_Matching
  def quoteCharsRegEx = "[\u0027\u2018\u02BB\u02BC\u2019\u05F3]".r

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

  def parseSign(signChar: Char): Parser[Some[SignPart]] =
    signChar match {
      case '+' => signChar.toString ^^^ Some(SignPart(true))
      case '-' => signChar.toString ^^^ Some(SignPart(false))
    }

  def parseNumberWithPossibleExponent(
      format: ArrayCursor[Char]
  ): Array[Parser[Option[ParsedNumberPart]]] = {
    var numberEnded = false
    var parserParts: Array[Parser[Option[ParsedNumberPart]]] = Array(
      // Parse an (optional) sign at the beginning of the number.
      // Yes, the format may specify a different position for the sign char but we only count the *first* one we come
      // across which should enable sensible behaviour.
      opt("+" ^^^ SignPart(true) | "-" ^^^ SignPart(false))
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
        case 'E' if surroundedByNumericDigits(format) =>
          val exponentDigits = parseDigits(format, false) ^^ (_.get)
          parserParts :+= "E" ~ exponentDigits ^^ {
            case _ ~ digits => Some(ExponentPart(digits))
          }
        case _ =>
          numberEnded = true
          // Step back one position in the format and let the parser continue from there.
          format.stepBack()
      }
    }

    parserParts
  }

  private def surroundedByNumericDigits(positionInFormat: ArrayCursor[Char]) =
    positionInFormat.hasPrevious() &&
      numericDigitChars.contains(positionInFormat.peekPrevious()) &&
      positionInFormat.hasNext() &&
      numericDigitChars.contains(positionInFormat.peekNext())

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
        Some(FractionalPart(digits))
      else
        Some(IntegerPart(digits))
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
    var parserParts: Array[Parser[Option[ParsedNumberPart]]] = Array()
    var hasDigitsParsingPart: Boolean = false

    while (format.hasNext()) {
      val nextChar = format.next()
      nextChar match {
        case '\'' =>
          parseQuote(format) match {
            case Right(quoteParser) => parserParts :+= quoteParser
            case Left(err)          => throw NumberFormatError(err)
          }
        case c if numericDigitChars.contains(c) =>
          format.stepBack()
          parserParts ++= parseNumberWithPossibleExponent(format)
          hasDigitsParsingPart = true
        case char @ ('-' | '+') => parserParts :+= parseSign(char)
        case char @ '%' =>
          parserParts :+= char.toString ^^^ Some(PercentagePart())
        case char @ '‰' =>
          parserParts :+= char.toString ^^^ Some(PerMillePart())
        case char @ '¤' => parserParts :+= char.toString ^^^ None
        // todo: Need to parse sub-patterns separated by ';'.
        // todo: Need to deal with special padding chars
        case char =>
          // http://www.unicode.org/reports/tr35/tr35-numbers.html#Special_Pattern_Characters
          // "Many characters in a pattern are taken literally; they are matched during parsing and output
          //  unchanged during formatting"
          parserParts :+= char.toString ^^^ None // Be permissive.
      }
    }

    if (!hasDigitsParsingPart) {
      throw NumberFormatError(
        "Number format does not contain any digit characters."
      )
    }

    getParserForNumberParts(parserParts)
  }

  private def getParserForNumberParts(
      parserParts: Array[Parser[Option[ParsedNumberPart]]]
  ): Parser[BigDecimal] =
    Parser { p =>
      val parsedNumber: ParsedNumber = ParsedNumber()
      var parsingError: Option[Failure] = None
      var currentInputPosition: Input = p

      for (numericPartParser <- parserParts) {
        if (parsingError.isEmpty) {
          numericPartParser(currentInputPosition) match {
            case Success(numberPart, rest) =>
              // https://www.unicode.org/reports/tr35/tr35-numbers.html#Parsing_Numbers
              // "If more than one sign, currency symbol, exponent, or percent/per mille occurs in the input,
              //  the first found should be used."
              numberPart match {
                case Some(s @ SignPart(_)) =>
                  if (parsedNumber.sign.isEmpty) parsedNumber.sign = Some(s)
                case Some(i @ IntegerPart(_)) => parsedNumber.integer = Some(i)
                case Some(f @ FractionalPart(_)) =>
                  parsedNumber.fraction = Some(f)
                case Some(e @ ExponentPart(_)) =>
                  if (parsedNumber.exponent.isEmpty)
                    parsedNumber.exponent = Some(e)
                case Some(factor: ScalingFactorPart) =>
                  if (parsedNumber.scalingFactor.isEmpty)
                    parsedNumber.scalingFactor = Some(factor)
                case None =>
              }
              currentInputPosition = rest
            case error @ Failure(_, rest) =>
              currentInputPosition = rest
              parsingError = Some(error)
          }
        }
      }

      parsingError
        .getOrElse(Success(parsedNumber.toBigDecimal(), currentInputPosition))
    }
}
