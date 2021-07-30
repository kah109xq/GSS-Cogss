package CSVValidation
import com.fasterxml.jackson.databind.node.{ArrayNode, BooleanNode, IntNode, JsonNodeFactory, ObjectNode, TextNode, ValueNode}
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.scalatest.FunSuite


class PropertyCheckerTest extends FunSuite {
  test("boolean property checker should return correct types") {
    val propertyChecker = new PropertyChecker("suppressOutput", new TextNode("w3c"), "https://www.w3.org/", "und")
    val result = propertyChecker.checkProperty()
    assert(result._1.isInstanceOf[Boolean])
    assert(result._2.isInstanceOf[String])
    assert(result._3.isInstanceOf[PropertyType.Value])
  }

  test("boolean property checker should return invalid for non boolean values") {
    val propertyChecker = new PropertyChecker("suppressOutput", new TextNode("w3c"), "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
    assert(value === false)
  }

  test("boolean property checker should return zero warnings for boolean values") {
    val propertyChecker = new PropertyChecker("suppressOutput", BooleanNode.getTrue, "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(value === true)
    assert(warnings === "")
  }

  test("language property checker should return invalid for properties which cannot be accepted") {
    val propertyChecker = new PropertyChecker("@language", new TextNode("Invalid Language Property"), "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("language property checker should return no warnings for correct language property") {
    val propertyChecker = new PropertyChecker("@language", new TextNode("sgn-BE-FR"), "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "")
  }

  test("metadata exception is thrown for invalid value while checking link property") {

    val thrown = intercept [MetadataError] {
      val propertyChecker = new PropertyChecker("@base", new TextNode("_:invalid"), "https://www.w3.org/", "und")
      propertyChecker.checkProperty()
    }
    assert(thrown.getMessage === "URL _:invalid starts with _:")
  }

  test("link property checker should return joined url after validation if baseUrl supplied") {
    val propertyChecker = new PropertyChecker("@id", new TextNode("csv-w"), "https://www.w3.org/", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(value === "https://www.w3.org/csv-w")
  }

  test("link property checker should return value as url after validation if baseUrl not supplied") {
    val propertyChecker = new PropertyChecker("@base", new TextNode("https://baseUrlNotSupplied/csv-w"), "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(value === "https://baseUrlNotSupplied/csv-w")
  }

  test("notes property checker should return invalid if value is not of type array") {
    val propertyChecker = new PropertyChecker("notes", new TextNode("Notes Content"), "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
    assert(value === false)
  }

  test("notes property checker returns values, warnings array for valid input") {
//    val notesArray = Array("firstNote", "SecondNote", "ThirdNote")
    var arrayNode = JsonNodeFactory.instance.arrayNode()
    arrayNode.add("FirstNote")
    arrayNode.add("secondNote") // Find a better way initialize ArrayNode
    val propertyChecker = new PropertyChecker("notes",  arrayNode, "", "und")
    val (values, warnings, typeString) = propertyChecker.checkProperty()
    assert(values.isInstanceOf[Array[_]])
    assert(warnings.isInstanceOf[Array[_]])
    // TODO test if the values array is same as what is expected
  }

  test("string property checker returns invalid warning if passed value is not string") {
    val propertyChecker = new PropertyChecker("default", BooleanNode.getFalse, "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("string property checker returns string value without warnings if passed value is string") {
    val propertyChecker = new PropertyChecker("default", new TextNode("sample string"), "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === null)
    assert(value === "sample string")
  }

  test("numeric property returns invalid on negative values") {
    val propertyChecker = new PropertyChecker("headerRowCount", new IntNode(-10), "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("numeric property returns value without warnings on valid value") {
    val propertyChecker = new PropertyChecker("headerRowCount", new IntNode(5), "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === null)
    assert(value === 5)
  }

  test("null property returns value in array without warnings on valid value") {
    val propertyChecker = new PropertyChecker("null", new TextNode("sample value"), "", "und")
    val (values, warnings, typeString) = propertyChecker.checkProperty()
    val expectedValues = Array[String]("sample value")
    assert(warnings === null)
    assert(values === expectedValues)
  }

  test("null property returns warnings for invalid value (non string type)") {
    val propertyChecker = new PropertyChecker("null", BooleanNode.getFalse, "", "und")
    val (value, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("null property with values array holding different types") {
    val values = JsonNodeFactory.instance.arrayNode()
    values.add("sample")
    values.add(5)
    val propertyChecker = new PropertyChecker("null", values, "", "und")
    val (returnedValues, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === Array[String]("invalid_value"))
    assert(returnedValues === Array[String]("sample"))
  }

  test("separator property returns invalid warning if not of type string or null") {
    val propertyChecker = new PropertyChecker("separator", BooleanNode.getFalse, "", "und")
    val (returnedValues, warnings, typeString) = propertyChecker.checkProperty()
    assert(warnings === "invalid_value")
  }

  test("separator property returns value on valid case") {
    val propertyChecker = new PropertyChecker("separator", JsonNodeFactory.instance.objectNode(), "", "und")
    val (returnedValue, _, _) = propertyChecker.checkProperty()
    assert(returnedValue === null)
  }

  test("datatype property checker returns expected value for object with correct base and format") {
    val json = "{\"base\": \"string\", \"format\": \"^The Sample RegEx$\"}"
    val objectMapper = new ObjectMapper
    val jsonNode = objectMapper.readTree(json)
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("base", "http://www.w3.org/2001/XMLSchema#string")
    expectedJsonNode.put("format", "^The Sample RegEx$")

    val propertyChecker = new PropertyChecker("datatype", jsonNode, "", "und")
    val (returnedValue, warnings, _) = propertyChecker.checkProperty()

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]())
  }

  test("datatype property checker returns expected value for object with invalid base and format") {
    val json = "{\"base\": \"invalidDatatypeSupplied\", \"format\": \"^The Sample RegEx$\"}"
    val objectMapper = new ObjectMapper
    val jsonNode = objectMapper.readTree(json)
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("base", "http://www.w3.org/2001/XMLSchema#string")
    expectedJsonNode.put("format", "^The Sample RegEx$")

    val propertyChecker = new PropertyChecker("datatype", jsonNode, "", "und")
    val (returnedValue, warnings, _) = propertyChecker.checkProperty()

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]("invalid_datatype_base"))
  }

  test("datatype property checker returns expected value for integer datatype") {
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("@id", "http://www.w3.org/2001/XMLSchema#integer")

    val propertyChecker = new PropertyChecker("datatype", new TextNode("integer"), "", "und")
    val (returnedValue, warnings, _) = propertyChecker.checkProperty()

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]())
  }

  test("datatype property checker returns expected warnings and values for invalid datatype") {
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("@id", "http://www.w3.org/2001/XMLSchema#string")

    val propertyChecker = new PropertyChecker("datatype", new TextNode("InvalidDataTypeSupplied"), "", "und")
    val (returnedValue, warnings, typeString) = propertyChecker.checkProperty()

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]("invalid_value"))
    assert(typeString === PropertyType.Inherited)
  }


}
