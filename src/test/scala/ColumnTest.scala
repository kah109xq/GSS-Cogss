package CSVValidation
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.{
  IntNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import org.scalatest.FunSuite

class ColumnTest extends FunSuite {
  val objectMapper = new ObjectMapper()
  test("should provide appropriate default values") {
    val json =
      """
        |{
        |"name":"countryCode"
        |}"
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val column = Column.fromJson(
      1,
      jsonNode.asInstanceOf[ObjectNode],
      "https://www.w3.org/",
      "und",
      JsonNodeFactory.instance.objectNode()
    )

    val datatypeDefaultValue: ObjectNode = JsonNodeFactory.instance
      .objectNode()
      .set("@id", new TextNode("http://www.w3.org/2001/XMLSchema#string"))

    assert(column.name.get === "countryCode")
    assert(column.datatype === datatypeDefaultValue)
    assert(column.lang === "und")
    assert(column.textDirection === "inherit")
    assert(column.annotations === Map[String, JsonNode]())
    assert(!column.virtual)
    assert(column.number == 1)
    assert(!column.ordered)
    assert(!column.required)
    assert(!column.suppressOutput)
    assert(column.aboutUrl.isEmpty)
    assert(column.default == "")
    assert(column.propertyUrl.isEmpty)
    assert(column.separator.isEmpty)
    assert(column.titles.isEmpty)
  }

  test("should override default values") {
    val json =
      """
        |{
        |   "name": "countryCode",
        |   "titles": "countryCode",
        |   "propertyUrl": "http://www.geonames.org/ontology"
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val column = Column.fromJson(
      1,
      jsonNode.asInstanceOf[ObjectNode],
      "https://www.w3.org/",
      "und",
      JsonNodeFactory.instance.objectNode()
    )

    val expectedTitlesObject = JsonNodeFactory.instance.objectNode()
    val arrayNode = JsonNodeFactory.instance.arrayNode()
    arrayNode.add("countryCode")
    expectedTitlesObject.set("lang", arrayNode)

    assert(
      column.propertyUrl.get === "http://www.geonames.org/ontology"
    )
    assert(column.name.get === "countryCode")
    assert(column.titles.get === expectedTitlesObject)
  }

  test("it should generate warnings for invalid null values") {
    val json =
      """
        |{
        |   "name": "countryCode",
        |   "null": true
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val column = Column.fromJson(
      1,
      jsonNode.asInstanceOf[ObjectNode],
      "https://www.w3.org/",
      "und",
      JsonNodeFactory.instance.objectNode()
    )
    assert(column.warnings(0).`type` === "invalid_value")
    assert(column.warnings.length === 1)
  }

  test("should set the correct datatype") {
    val json =
      """
        |{ 
        |   "name": "Id", 
        |   "required": true, 
        |   "datatype": { "base": "string", "minLength": 3 }
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val column = Column.fromJson(
      1,
      jsonNode.asInstanceOf[ObjectNode],
      "https://www.w3.org/",
      "und",
      JsonNodeFactory.instance.objectNode()
    )
    val expectedDatatypeValue: ObjectNode = JsonNodeFactory.instance
      .objectNode()
    expectedDatatypeValue.set(
      "base",
      new TextNode("http://www.w3.org/2001/XMLSchema#string")
    )
    expectedDatatypeValue.set("minLength", new IntNode(3))

    assert(column.name.get === "Id")
    assert(column.required)
    assert(column.datatype === expectedDatatypeValue)
  }
}
