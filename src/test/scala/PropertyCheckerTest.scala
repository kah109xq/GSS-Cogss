package CSVValidation
import org.scalatest.FunSuite

class PropertyCheckerTest extends FunSuite {
  test("boolean property checker should return correct types") {
    val propertyChecker = new PropertyChecker("suppressOutput", "w3c", "https://www.w3.org/", "und")
    val result = propertyChecker.checkProperty()
    assert(result._1.isInstanceOf[Boolean])
    assert(result._2.isInstanceOf[String])
    assert(result._3.isInstanceOf[PropertyType.Value])
  }

  test("boolean property checker should return invalid for non boolean values") {
    val propertyChecker = new PropertyChecker("suppressOutput", "w3c", "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
    assert(value === false)
  }

  test("boolean property checker should return zero warnings for boolean values") {
    val propertyChecker = new PropertyChecker("suppressOutput", true, "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(value === true)
    assert(warnings === "")
  }

  test("language property checker should return invalid for properties which cannot be accepted") {
    val propertyChecker = new PropertyChecker("@language", "Invalid Language Property", "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("language property checker should return no warnings for correct language property") {
    val propertyChecker = new PropertyChecker("@language", "sgn-BE-FR", "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "")
  }

  test("metadata exception is thrown for invalid value while checking link property") {

    val thrown = intercept [MetadataError] {
      val propertyChecker = new PropertyChecker("@base", "_:invalid", "https://www.w3.org/", "und")
      propertyChecker.checkProperty()
    }
    assert(thrown.getMessage === "URL _:invalid starts with _:")
  }

  test("link property checker should return joined url after validation if baseUrl supplied") {
    val propertyChecker = new PropertyChecker("@id", "csv-w", "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(value === "https://www.w3.org/csv-w")
  }

  test("link property checker should return value as url after validation if baseUrl not supplied") {
    val propertyChecker = new PropertyChecker("@base", "https://baseUrlNotSupplied/csv-w", "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(value === "https://baseUrlNotSupplied/csv-w")
  }

  test("notes property checker should return invalid if value is not of type array") {
    val propertyChecker = new PropertyChecker("notes", "Notes Content", "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
    assert(value === false)
  }

  test("notes property checker returns values, warnings array for valid input") {
    val notesArray = Array("firstNote", "SecondNote", "ThirdNote")
    val propertyChecker = new PropertyChecker("notes", notesArray, "", "und")
    val (values, warnings, typeString) = propertyChecker.checkProperty()
    assert(values.isInstanceOf[Array[_]])
    assert(warnings.isInstanceOf[Array[_]])
    // TODO test if the values array is same as what is expected
  }

  test("string property checker returns invalid warning if passed value is not string") {
    val propertyChecker = new PropertyChecker("default", false, "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("string property checker returns string value without warnings if passed value is string") {
    val propertyChecker = new PropertyChecker("default", "sample string", "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === null)
    assert(value === "sample string")
  }



}




//  test("it should return a string array as the first element of tuple returned") {
//    val validator = new Validator("no_file_like_this.csv")
//    val result = validator.validate()
//    assert(result._1.isInstanceOf[Array[String]])
//  }
//
//  test("it should return a string array as the second element of tuple returned") {
//    val validator = new Validator("no_file_like_this.csv")
//    val result = validator.validate()
//    assert(result._2.isInstanceOf[Array[String]])
//  }
