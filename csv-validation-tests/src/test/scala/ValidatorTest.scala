package CSVValidation
import org.scalatest.FunSuite

class ValidatorTest extends FunSuite {
  test("it should return a tuple") {
    val validator = new Validator("no_file_like_this.csv")
    val result = validator.validate()
    assert(result.isInstanceOf[Tuple2[Array[String], Array[String]]])
  }

  test("it should return a string array as the first element of tuple returned") {
    val validator = new Validator("no_file_like_this.csv")
    val result = validator.validate()
    assert(result._1.isInstanceOf[Array[String]])
  }

  test("it should return a string array as the second element of tuple returned") {
    val validator = new Validator("no_file_like_this.csv")
    val result = validator.validate()
    assert(result._2.isInstanceOf[Array[String]])
  }
}