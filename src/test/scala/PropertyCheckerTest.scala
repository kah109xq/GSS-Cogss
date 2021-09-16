package CSVValidation
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node._
import org.scalatest.{FunSuite, Tag}

import scala.jdk.CollectionConverters.IteratorHasAsScala

class PropertyCheckerTest extends FunSuite {
  val objectMapper = new ObjectMapper()

  test("boolean property checker should return correct types") {
    val result = PropertyChecker.checkProperty("suppressOutput", new TextNode("w3c"), "https://www.w3.org/", "und")
    assert(result._1.isInstanceOf[BooleanNode])
    assert(result._2.isInstanceOf[Array[String]])
    assert(result._3.isInstanceOf[PropertyType.Value])
  }

  test("boolean property checker should return invalid for non boolean values") {
    val (value, warnings, typeString) = PropertyChecker.checkProperty("suppressOutput", new TextNode("w3c"), "https://www.w3.org/", "und")
    assert(warnings === Array[String]("invalid_value"))
    assert(value === BooleanNode.getFalse)
  }

  test("boolean property checker should return zero warnings for boolean values") {
    val (value, warnings, typeString) = PropertyChecker.checkProperty("suppressOutput", BooleanNode.getTrue, "https://www.w3.org/", "und")
    assert(value === BooleanNode.getTrue)
    assert(warnings.isEmpty)
  }

  test("language property checker should return invalid for properties which cannot be accepted") {
    val (_, warnings, _) = PropertyChecker.checkProperty("@language", new TextNode("Invalid Language Property"), "https://www.w3.org/", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("language property checker should return no warnings for correct language property") {
    val (_, warnings, _) = PropertyChecker.checkProperty("@language", new TextNode("sgn-BE-FR"), "https://www.w3.org/", "und")
    assert(warnings.isEmpty)
  }

  test("metadata exception is thrown for invalid value while checking link property") {
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("@base", new TextNode("_:invalid"), "https://www.w3.org/", "und")
    }
    assert(thrown.getMessage === "URL _:invalid starts with _:")
  }

  test("link property checker should return joined url after validation if baseUrl supplied") {
    val (value, _, _) = PropertyChecker.checkProperty("@id", new TextNode("csv-w"), "https://www.w3.org/", "und")
    assert(value === new TextNode("https://www.w3.org/csv-w"))
  }

  test("link property checker should return value as url after validation if baseUrl not supplied") {
    val (value, _, _) = PropertyChecker.checkProperty("@base", new TextNode("https://baseUrlNotSupplied/csv-w"), "", "und")
    assert(value === new TextNode("https://baseUrlNotSupplied/csv-w"))
  }

  test("notes property checker should return invalid if value is not of type array") {
    val (value, warnings, _) = PropertyChecker.checkProperty("notes", new TextNode("Notes Content"), "", "und")
    
    assert(warnings === Array[String]("invalid_value"))
    assert(value.isInstanceOf[ArrayNode])
  }

  test("notes property checker returns values, warnings array for valid input") {
    //    val notesArray = Array("firstNote", "SecondNote", "ThirdNote")
    var arrayNode = JsonNodeFactory.instance.arrayNode()
    arrayNode.add("FirstNote")
    arrayNode.add("secondNote") // Find a better way initialize ArrayNode
    val (values, warnings, _) = PropertyChecker.checkProperty("notes", arrayNode, "", "und")
    assert(values.isInstanceOf[ArrayNode])
    assert(warnings.isInstanceOf[Array[String]])
    val arrayNodeOut = values.asInstanceOf[ArrayNode]
    val notes = Array.from(arrayNodeOut.elements.asScala)
    assert(notes(0).isInstanceOf[TextNode])
    assert(notes(0).asText() == "FirstNote")
    assert(notes(1).isInstanceOf[TextNode])
    assert(notes(1).asText() == "secondNote")
  }

  test("string property checker returns invalid warning if passed value is not string") {
    val (_, warnings, _) = PropertyChecker.checkProperty("default", BooleanNode.getFalse, "", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("string property checker returns string value without warnings if passed value is string") {
    val (value, warnings, _) = PropertyChecker.checkProperty("default", new TextNode("sample string"), "", "und")
    assert(warnings === Array[String]())
    assert(value === new TextNode("sample string"))
  }

  test("numeric property returns invalid on negative values") {
    val (_, warnings, _) = PropertyChecker.checkProperty("headerRowCount", new IntNode(-10), "", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("numeric property returns value without warnings on valid value") {
    val (value, warnings, _) = PropertyChecker.checkProperty("headerRowCount", new IntNode(5), "", "und")
    assert(warnings === Array[String]())
    assert(value === new IntNode(5))
  }

  test("null property returns value in array without warnings on valid value") {
    val (values, warnings, _) = PropertyChecker.checkProperty("null", new TextNode("sample value"), "", "und")
    var expectedValues = new TextNode("sample value")
    assert(warnings === Array[String]())
    assert(values === expectedValues)
  }

  test("null property returns warnings for invalid value (non string type)") {
    val (_, warnings, _) = PropertyChecker.checkProperty("null", BooleanNode.getFalse, "", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("null property with values array holding different types") {
    val values = JsonNodeFactory.instance.arrayNode()
    values.add("sample")
    values.add(5)
    val expectedValues = JsonNodeFactory.instance.arrayNode()
    expectedValues.add("sample")

    val (returnedValues, warnings, _) = PropertyChecker.checkProperty("null", values, "", "und")

    assert(warnings === Array[String]("invalid_value"))
    assert(returnedValues === expectedValues)
  }

  test("separator property returns invalid warning if not of type string or null") {
    val (_, warnings, _) = PropertyChecker.checkProperty("separator", BooleanNode.getFalse, "", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("separator property returns value on valid case") {
    val (returnedValue, _, _) = PropertyChecker.checkProperty("separator", JsonNodeFactory.instance.objectNode(), "", "und")
    assert(returnedValue === NullNode.getInstance())
  }

  test("datatype property checker returns expected value for object with correct base and format") {
    val json = "{\"base\": \"string\", \"format\": \"^The Sample RegEx$\"}"
    val jsonNode = objectMapper.readTree(json)
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("base", "http://www.w3.org/2001/XMLSchema#string")
    expectedJsonNode.put("format", "^The Sample RegEx$")

    val (returnedValue, warnings, _) = PropertyChecker.checkProperty("datatype", jsonNode, "", "und")

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]())
  }

  val t = Tag("Some Group")
  test("datatype property checker returns expected value for object with invalid base and format", t) {
    val json =
      """
        |{
        | "base": "invalidDatatypeSupplied",
        | "format": "^The Sample RegEx$"
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("base", "http://www.w3.org/2001/XMLSchema#string")
    expectedJsonNode.put("format", "^The Sample RegEx$")

    val (returnedValue, warnings, _) = PropertyChecker.checkProperty("datatype", jsonNode, "", "und")

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]("invalid_datatype_base"))
  }

  test("datatype object cannot have @id of builtInDataTypes", t) {
    val json =
      """
        |{
        | "@id": "string"
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)

    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatype @id must not be the id of a built-in datatype (string)")
  }

  test("datatype property checker returns expected value for integer datatype", t) {
    // move to multi line string .
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("@id", "http://www.w3.org/2001/XMLSchema#integer")

    val (returnedValue, warnings, _) = PropertyChecker.checkProperty("datatype", new TextNode("integer"), "", "und")

    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]())
  }

  test("datatype property checker returns expected warnings and values for invalid datatype") {
    val expectedJsonNode = JsonNodeFactory.instance.objectNode()
    expectedJsonNode.put("@id", "http://www.w3.org/2001/XMLSchema#string")

    val (returnedValue, warnings, typeString) = PropertyChecker.checkProperty("datatype", new TextNode("InvalidDataTypeSupplied"), "", "und")
    assert(returnedValue === expectedJsonNode)
    assert(warnings === Array[String]("invalid_value"))
    assert(typeString === PropertyType.Inherited)
  }

  test("datatype object with integer base cannot have length facet") {
    val json =
      """
        |{
        | "base": "integer",
        | "length": 10
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatypes based on http://www.w3.org/2001/XMLSchema#integer cannot have a length facet")
  }

  test("datatype object with integer base cannot have minLength facet") {
    val json =
      """
        |{
        | "base": "integer",
        | "minLength": 10
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatypes based on http://www.w3.org/2001/XMLSchema#integer cannot have a minLength facet")
  }

  test("datatype object with integer base cannot have maxLength facet") {
    val json =
      """
        |{
        | "base": "integer",
        | "maxLength": 10
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatypes based on http://www.w3.org/2001/XMLSchema#integer cannot have a maxLength facet")
  }

  test("datatype object with any base datatype other than numeric or datetime cannot have minInclusive") {
    val json =
      """
        |{
        | "base": "string",
        | "minInclusive": 10
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "minInclusive is only allowed for numeric, date/time and duration types")
  }

  test("datatype object with any base datatype other than numeric or datetime cannot have maxExclusive") {
    val json =
      """
        |{
        | "base": "string",
        | "maxExclusive": 10
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "maxExclusive is only allowed for numeric, date/time and duration types")
  }
  // 2 more similar tests for minExclusive and maxInclusive can be added

  test("datatype object cannot specify both minInclusive and minExclusive") {
    val json =
      """
        |{
        | "base": "decimal",
        | "minInclusive": 10,
        | "minExclusive": 10
        |}
        |""".stripMargin //

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatype cannot specify both minimum/minInclusive (10) and minExclusive (10)")
  }

  test("datatype object cannot specify both minInclusive greater than maxInclusive") {
    val json =
      """
        |{
        | "base": "decimal",
        | "minInclusive": 10,
        | "maxInclusive": 9
        |}
        |""".stripMargin //

    val jsonNode = objectMapper.readTree(json)

    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatype minInclusive (10) cannot be more than maxInclusive (9)")
  }

  test("datatype minInclusive cannot be more than or equal to maxExclusive") {
    val json =
      """
        |{
        | "base": "decimal",
        | "minInclusive": 10,
        | "maxExclusive": 10
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    }
    assert(thrown.getMessage === "datatype minInclusive (10) cannot be greater than or equal to maxExclusive (10)")
  }

  test("datatype check invalid min/max ranges throw exception") {
    val jsonArray = Array[String](
      """
        |{
        | "base": "decimal",
        | "minInclusive": 10,
        | "maxExclusive": 10
        |}
        |""".stripMargin,
      """
        |{
        | "base": "decimal",
        | "minInclusive": 10,
        | "maxInclusive": 9
        |}
        |""".stripMargin,
      """
        |{
        | "base": "decimal",
        | "minInclusive": 10,
        | "minExclusive": 10
        |}
        |""".stripMargin,
      """
        |{
        | "base": "decimal",
        | "minExclusive": 11,
        | "maxExclusive": 10
        |}
        |""".stripMargin,
      """
        |{
        | "base": "decimal",
        | "minExclusive": 11,
        | "maxInclusive": 10
        |}
        |""".stripMargin,
    )

    for (json <- jsonArray) {
      val jsonNode = objectMapper.readTree(json)
      val thrown = intercept[MetadataError] {
        PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
      }
    }
  }

  test("datatype check invalid min/max lengths throw exception") {
    val jsonArray = Array[String](
      """
        |{
        | "base": "decimal",
        | "minLength": 10,
        | "length": 9
        |}
        |""".stripMargin,
      """
        |{
        | "base": "decimal",
        | "maxLength": 8,
        | "length": 9
        |}
        |""".stripMargin,
      """
        |{
        | "base": "decimal",
        | "maxLength": 10,
        | "length": 9
        |}
        |""".stripMargin
    )

    for (json <- jsonArray) {
      val jsonNode = objectMapper.readTree(json)
      val thrown = intercept[MetadataError] {
        PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
      }
    }
  }

  test("returns invalid boolean format warning if wrong boolean type format") {
    val json = """
                 |{
                 | "format": "YES|NO|MAYBE",
                 | "base": "boolean"
                 |}
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (_, warnings, _) = PropertyChecker.checkProperty("datatype", jsonNode, "", "und")
    assert(warnings === Array[String]("invalid_boolean_format"))
  }

  test("returns expected object node when valid boolean type format supplied") {
    val json = """
                 |{
                 | "format": "YES|NO",
                 | "base": "boolean"
                 |}
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val expectedValue = JsonNodeFactory.instance.objectNode()
    val arrayNodeObject = JsonNodeFactory.instance.arrayNode()
    arrayNodeObject.add("YES")
    arrayNodeObject.add("NO")
    expectedValue.set("format", arrayNodeObject)
    expectedValue.put("base", "http://www.w3.org/2001/XMLSchema#boolean")

    val (returnedValue, warnings, _) = PropertyChecker.checkProperty("datatype", jsonNode, "", "und")

    assert(warnings === Array[String]())
    assert(returnedValue === expectedValue)
  }


  test("returns expected object node when valid regexp datatype is supplied in base") {
    val json = """
                 |{
                 | "format": "0xabcdefg",
                 | "base": "hexBinary"
                 |}
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val expectedValue = JsonNodeFactory.instance.objectNode()
    expectedValue.put("format", "0xabcdefg")
    expectedValue.put("base", "http://www.w3.org/2001/XMLSchema#hexBinary")

    val (returnedValue, warnings, _) = PropertyChecker.checkProperty("datatype", jsonNode, "", "und")

    assert(warnings === Array[String]())
    assert(returnedValue === expectedValue)
  }

  test("throw metadata error if id starts with _:") {
//    val uri = new TextNode("www.sampleurl.com")
//    val (value, warnings, typeString) = PropertyChecker.checkProperty("tableSchema", uri, baseUrl = "https://chickenburgers.com", "und")
    val json = """
                 |{
                 | "@id": "_:someValue"
                 |}
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("tableSchema", jsonNode, "http://www.w3.org/", "und")
    }
    assert(thrown.getMessage === "@id _:someValue starts with _:")
  }

  test("throw metadata error if @type of schema is not 'Schema'") {
    val json = """
                 |{
                 | "@type": "someValueOtherThanSchema"
                 |}
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("tableSchema", jsonNode, "http://www.w3.org/", "und")
    }
    assert(thrown.getMessage === "@type of schema is not 'Schema'")
  }

  test("return invalid value warning if tableSchema property is not a string or an object") {
    val (_, warnings, _) = PropertyChecker.checkProperty("tableSchema", BooleanNode.getTrue, baseUrl = "https://chickenburgers.com", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("return expected schemaJson after validation (remove properties test)") {
    val json = """
                 |{
                 | "@id": "https://chickenburgers.com",
                 | "notes": "notesContent"
                 | }
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    jsonNode.asInstanceOf[ObjectNode].remove("notes")
    val (schema, warnings, _) = PropertyChecker.checkProperty("tableSchema", jsonNode, baseUrl = "https://chickenburgers.com", "und")

    assert(warnings === Array[String]())
    assert(schema === jsonNode) // Notes property should be stripped of after validation as it does not come under schema or inherited
  }

  test("return expected schemaJson after validation") {
    val json = """
                 |{
                 | "@id": "https://chickenburgers.com",
                 | "separator": "separatorContent"
                 | }
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (schema, warnings, _) = PropertyChecker.checkProperty("tableSchema", jsonNode, baseUrl = "https://chickenburgers.com", "und")

    assert(warnings === Array[String]())
    assert(schema === jsonNode) // separator property should Not be stripped of after validation as it comes under inherited
  }

  // ForeignKeys property tests
  test("throw metadata error when property foreignKey property value contains colon") {
    val json = """
                 |[
                 |{"@id": "https://chickenburgers.com"},
                 |{"contain:colon": "separatorContent"}
                 |]
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("foreignKeys", jsonNode, baseUrl = "https://chickenburgers.com", "und")
    }
    assert(thrown.getMessage === "foreignKey includes a prefixed (common) property")

  }

  test("return invalid value warning if foreignKeys property value is not array") {
    val json = """
                 |{
                 | "@id": "https://chickenburgers.com",
                 | ":separator": "separatorContent"
                 | }
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (values, warnings, _) = PropertyChecker.checkProperty("foreignKeys", jsonNode, baseUrl = "https://chickenburgers.com", "und")
    assert(warnings === Array[String]("invalid_value"))
  }

  test("return correct jsonNode with property removed and warnings if property is not valid") {
    val json =
      """
        |[
        |{"datatype": "invalidTextDataSupplied"}
        |]
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (values, warnings, _) = PropertyChecker.checkProperty("foreignKeys", jsonNode, baseUrl = "https://chickenburgers.com", "und")
    assert(warnings.contains("invalid_value"))
    assert(values.path("datatype").isMissingNode)
  }

  // Reference Property tests
  test("throw metadata error when foreign key reference is not an object") {
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("reference", new TextNode("Some text value"), "", "und")
    }
    assert(thrown.getMessage === "foreignKey reference is not an object")
  }

  test("throw metadata error when property reference property value contains colon") {
    val json = """
                 |{
                 |  "contain:colon": "some content"
                 | }
                 |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val thrown = intercept[MetadataError] {
      PropertyChecker.checkProperty("reference", jsonNode, baseUrl = "https://chickenburgers.com", "und")
    }
    assert(thrown.getMessage === "foreignKey reference (contain:colon) includes a prefixed (common) property")
  }
  // Add more test cases for referenceProperty after resource, schemaReference, columnReference property validations
  // are implemented. Currently the exceptions raised when these properties are missing is not tested since
  // NoSuchElementExceptio is thrown when a jsonnode with these properties are passed in.
}
