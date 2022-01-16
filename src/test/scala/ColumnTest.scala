package CSVValidation
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.{
  IntNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import org.scalatest.FunSuite

import scala.collection.mutable
import scala.collection.mutable.Map

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
      Map[String, JsonNode]()
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
    assert(column.columnOrdinal == 1)
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
        |   "propertyUrl": "http://www.geonames.org/ontology",
        |   "aboutUrl": "sampleUrl",
        |   "datatype": "integer",
        |   "lang": "en",
        |   "default": "00",
        |   "null": "-",
        |   "ordered": true,
        |   "propertyUrl": "http://www.geonames.org/ontology",
        |   "required": true, 
        |   "separator": ",",
        |   "suppressOutput": true,
        |   "textDirection": "rtl",
        |   "titles": [ "countryCode" ],
        |   "valueUrl": "http://www.geonames.org/ontology",
        |   "virtual": true
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val column = Column.fromJson(
      1,
      jsonNode.asInstanceOf[ObjectNode],
      "https://www.w3.org/",
      "und",
      mutable.Map()
    )

    val expectedTitlesObject = JsonNodeFactory.instance.objectNode()
    val expectedDataType = JsonNodeFactory.instance.objectNode()
    expectedDataType.set(
      "@id",
      new TextNode("http://www.w3.org/2001/XMLSchema#integer")
    )
    val arrayNode = JsonNodeFactory.instance.arrayNode()
    arrayNode.add("countryCode")
    expectedTitlesObject.set("lang", arrayNode)

    assert(column.name.get === "countryCode")
    assert(column.columnOrdinal === 1)
    assert(column.id === None)
    assert(column.aboutUrl.get === "sampleUrl")
    assert(column.datatype === expectedDataType)
    assert(column.default === "00")
    assert(column.lang === "en")
    assert(column.nullParam === Array[String]("-"))
    assert(column.ordered === true)
    assert(column.propertyUrl.get === "http://www.geonames.org/ontology")
    assert(column.required === true)
    assert(column.separator.get === ",")
    assert(column.suppressOutput === true)
    assert(column.textDirection === "rtl")
    assert(column.titles.get === expectedTitlesObject)
    assert(column.valueUrl.get === "http://www.geonames.org/ontology")
    assert(column.virtual === true)
    assert(column.annotations === mutable.Map[String, JsonNode]())
    assert(column.warnings === Array[ErrorMessage]())
  }

  test("it should generate warnings for null values of unexpected type") {
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
      Map()
    )
    assert(column.warnings(0).`type` === "invalid_value")
    assert(column.warnings(0).content === "null: true")
    assert(column.warnings.length === 1)
  }

  test("it should return no warnings for a null value of null") {

    val json =
      """
        |{
        |   "name": "countryCode",
        |   "null": null
        |}
        |""".stripMargin

    val jsonNode = objectMapper.readTree(json)
    val column = Column.fromJson(
      1,
      jsonNode.asInstanceOf[ObjectNode],
      "https://www.w3.org/",
      "und",
      Map()
    )
    assert(column.warnings.length === 0)
    assert(column.nullParam === Array[String](""))
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
      Map()
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
