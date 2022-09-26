package CSVValidation

/**
  * Part of a parsed number.
  */
abstract class ParsedNumberPart

/**
  * Applies a `scalingFactor` to the parsed number. e.g. 50% can be parsed as 0.5
  */
abstract class ScalingFactorPart extends ParsedNumberPart {
  val scalingFactor: Float
}

case class PercentagePart() extends ScalingFactorPart {
  override val scalingFactor: Float = 0.01f
}

case class PerMillePart() extends ScalingFactorPart {
  override val scalingFactor: Float = 0.001f
}

/**
  * Indicates whether the parsed number is positive or negative.
  * @param isPositive
  */
case class SignPart(isPositive: Boolean) extends ParsedNumberPart

abstract class DigitsPart extends ParsedNumberPart {
  val digits: String
}

case class IntegerDigitsPart(digits: String) extends DigitsPart

case class FractionalDigitsPart(digits: String) extends DigitsPart

case class ExponentPart(isPositive: Boolean, exponent: DigitsPart)
    extends ParsedNumberPart {
  override def toString: String = {
    val sign = if (isPositive) "+" else "-"
    "E" + sign + exponent.digits
  }
}

/**
  * Brings together the parts of a parsed number and allows conversion to a numeric representation.
  * @param sign
  * @param integerDigits
  * @param fractionalDigits
  * @param exponent
  */
case class ParsedNumber(
    var sign: Option[SignPart] = None,
    var integerDigits: Option[IntegerDigitsPart] = None,
    var fractionalDigits: Option[FractionalDigitsPart] = None,
    var exponent: Option[ExponentPart] = None,
    var scalingFactor: Option[ScalingFactorPart] = None
) {
  def toBigDecimal(): BigDecimal = {
    val signPart = sign.map(s => if (s.isPositive) "+" else "-").getOrElse("+")
    val integerPart = integerDigits.map(i => i.digits).getOrElse("0")
    val fractionalPart = fractionalDigits.map(f => "." + f.digits).getOrElse("")
    val exponentPart = exponent.map(_.toString).getOrElse("")

    val normalisedNumberString =
      signPart + integerPart + fractionalPart + exponentPart

    val factorToScaleNumberBy =
      scalingFactor.map(f => f.scalingFactor).getOrElse(1.0f)

    BigDecimal(normalisedNumberString).*(
      BigDecimal.decimal(factorToScaleNumberBy)
    )
  }
}
