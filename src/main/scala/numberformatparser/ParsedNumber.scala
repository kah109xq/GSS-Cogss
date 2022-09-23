package CSVValidation

import com.ibm.icu.text.DecimalFormat

abstract class ParsedNumberPart

abstract class ScalingFactorPart extends ParsedNumberPart {
  val scalingFactor: Float
}

case class PercentagePart() extends ScalingFactorPart {
  override val scalingFactor: Float = 0.01f
}

case class PerMillePart() extends ScalingFactorPart {
  override val scalingFactor: Float = 0.001f
}

case class SignPart(isPositive: Boolean) extends ParsedNumberPart

abstract class DigitsPart extends ParsedNumberPart {
  val digits: String
}

case class IntegerPart(digits: String) extends DigitsPart

case class FractionalPart(digits: String) extends DigitsPart

case class ExponentPart(exponent: DigitsPart) extends ParsedNumberPart

object ParsedNumber {
  private val df: DecimalFormat = new DecimalFormat()
}

/**
  * Brings together the parts of a parsed number and allows conversion to a numeric representation.
  * @param sign
  * @param integer
  * @param fraction
  * @param exponent
  */
case class ParsedNumber(
    var sign: Option[SignPart] = None,
    var integer: Option[IntegerPart] = None,
    var fraction: Option[FractionalPart] = None,
    var exponent: Option[ExponentPart] = None,
    var scalingFactor: Option[ScalingFactorPart] = None
) {
  def toBigDecimal(): BigDecimal = {
    val signPart = sign.map(s => if (s.isPositive) "+" else "-").getOrElse("+")
    val integerPart = integer.map(i => i.digits).getOrElse("0")
    val fractionalPart = fraction.map(f => "." + f.digits).getOrElse("")
    val exponentPart = exponent.map(e => "E" + e.exponent.digits).getOrElse("")

    val normalisedNumberString =
      signPart + integerPart + fractionalPart + exponentPart

    val factorToScaleNumberBy =
      scalingFactor.map(f => f.scalingFactor).getOrElse(1.0f)

    BigDecimal(normalisedNumberString).*(
      BigDecimal.decimal(factorToScaleNumberBy)
    )
  }
}
