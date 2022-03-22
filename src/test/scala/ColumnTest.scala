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

  // Tests for processFloatDatatype method
  test("should process valid float datatype value without errors") {
    val result = Column.processFloatDatatype("4268.22752E11", None)
    assert(result.isRight)
    result match {
      case Right(floatValue) => {
        assert(floatValue == "4268.22752E11".toFloat)
      }
    }
  }

  test("should return error when invalid float value is processed") {
    val result =
      Column.processFloatDatatype(
        "-3E2.4",
        None
      ) //the exponent must be an integer
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_float")
      }
    }
  }

  // Tests for processDoubleDatatype method
  test("should process valid double datatype value without errors") {
    val result = Column.processDoubleDatatype("4268.22752E11", None)
    assert(result.isRight)
    result match {
      case Right(doubleDatatype) => {
        assert(doubleDatatype.toString == "4.26822752E14")
      }
    }
  }

  test(
    "should process valid double (negative infinity) datatype value without errors"
  ) {
    val result = Column.processDoubleDatatype("-INF", None)
    assert(result.isRight)
    result match {
      case Right(doubleValue) => {
        assert(doubleValue.isNegInfinity)
      }
    }
  }

  test("should return error when invalid double value is processed") {
    val result = Column.processDoubleDatatype("NAN", None)
    assert(
      result.isLeft
    ) //values are case-sensitive, must be capitalized correctly
    result match {
      case Left(error) => {
        assert(error == "invalid_double")
      }
    }
  }

  // Tests for processNegativeInteger
  test("should return error when invalid negative integer is processed") {
    val result = Column.processNegativeInteger("0", None)
    assert(
      result.isLeft
    ) // zero is not considered negative
    result match {
      case Left(error) => {
        assert(error == "invalid_negativeInteger")
      }
    }
  }

  test(
    "should process valid negativeInteger datatype value without errors"
  ) {
    val result =
      Column
        .processNegativeInteger("-00122", None) // leading zeros are permitted
    assert(result.isRight)
    result match {
      case Right(negativeInteger) => {
        assert(negativeInteger.toString == "-122")
      }
    }
  }

  // Tests for processNonPositiveInteger

  test("should return error when invalid NonPositiveInteger is processed") {
    val result =
      Column.processNonPositiveInteger(
        "3.0",
        None
      ) //value must not contain a decimal point
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_nonPositiveInteger")
      }
    }
  }

  test("should process valid non positive Integer value without errors") {
    val result = Column
      .processNonPositiveInteger("0", None) //
    assert(result.isRight)
    result match {
      case Right(nonPositiveInteger) => {
        assert(nonPositiveInteger.toString == "0")
      }
    }
  }

  // Tests for processUnsignedByte

  test("should process valid unsigned byte value without errors") {
    val result = Column
      .processUnsignedByte("+3", None) //
    assert(result.isRight)
    result match {
      case Right(unsignedByte) => {
        assert(unsignedByte.toString == "3")
      }
    }
  }

  test("should return errors when invalid unsigned byte value is processed") {
    val result =
      Column.processUnsignedByte(
        "256",
        None
      ) // Number is too large to be a unsigned byte
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_unsignedByte")
      }
    }
  }

  // Tests for processUnsignedShort
  test("should return error when invalid unsigned short value is processed") {
    val result =
      Column.processUnsignedShort(
        "-123", //negative values are not allowed
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_unsignedShort")
      }
    }
  }

  test("should process valid unsigned short datatype value without errors") {
    val result = Column
      .processUnsignedShort("0", None)
    assert(result.isRight)
    result match {
      case Right(unsignedShort) => {
        assert(unsignedShort.toString == "0")
      }
    }
  }

  // Tests for processUnsignedInt

  test("should process valid unsigned Int datatype value without errors") {
    val result = Column.processUnsignedInt("4545454", None)
    assert(result.isRight)
    result match {
      case Right(unsignedInt) => {
        assert(unsignedInt.toString == "4545454")
      }
    }
  }

  test("should return error when invalid unsigned int value is processed") {
    val result =
      Column.processUnsignedInt(
        "4294967299", // number is too large
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_unsignedInt")
      }
    }
  }

  //Tests for processUnsignedLong
  test("should return error when invalid unsigned Long value is processed") {
    val result =
      Column.processUnsignedLong(
        "18446744073709551620", // number is too large
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_unsignedLong")
      }
    }
  }

  test("should process valid unsigned Long datatype value without errors") {
    val result = Column.processUnsignedLong("+3", None)
    assert(result.isRight)
    result match {
      case Right(unsignedLong) => {
        assert(unsignedLong.toString == "3")
      }
    }
  }

  // Tests for processPositiveInteger
  test("should process valid positive integer datatype value without errors") {
    val result = Column.processPositiveInteger("00122", None)
    assert(result.isRight)
    result match {
      case Right(positiveInteger) => {
        assert(positiveInteger.toString() == "122")
      }
    }
  }

  test("should return error when invalid positive integer value is processed") {
    val result =
      Column.processPositiveInteger(
        "3.0",
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_positiveInteger")
      }
    }
  }

  // Tests for processNonNegativeInteger
  test(
    "should return error when invalid non negative integer value is processed"
  ) {
    val result =
      Column.processNonNegativeInteger(
        "-3",
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_nonNegativeInteger")
      }
    }
  }

  test(
    "should process valid non negative integer datatype value without errors"
  ) {
    val result = Column.processNonNegativeInteger("0", None)
    assert(result.isRight)
    result match {
      case Right(nonNegativeInteger) => {
        assert(nonNegativeInteger.toString() == "0")
      }
    }
  }

  // Tests for processByteDatatype
  test(
    "should process valid byte datatype value without errors"
  ) {
    val result = Column.processByteDatatype("-123", None)
    assert(result.isRight)
    result match {
      case Right(byteValue) => {
        assert(byteValue.toString == "-123")
      }
    }
  }

  test(
    "should return error when invalid byte value is processed"
  ) {
    val result =
      Column.processByteDatatype(
        "2.23",
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_byte")
      }
    }
  }

  // Tests for processShortDatatype
  test(
    "should return error when invalid short value is processed"
  ) {
    val result =
      Column.processShortDatatype(
        "32770",
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_short")
      }
    }
  }

  test("should process valid short datatype value without errors") {
    val result = Column.processShortDatatype("-1231", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value.toString == "-1231")
      }
    }
  }

  // Tests for processIntDatatype
  test("should process valid int datatype value without errors") {
    val result = Column.processIntDatatype("-12312", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value.toString == "-12312")
      }
    }
  }

  test(
    "should return error when invalid int value is processed"
  ) {
    val result =
      Column.processIntDatatype(
        "2147483650", //number too large
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(error == "invalid_int - '2147483650' Outside Int Range")
      }
    }
  }

  // Tests for processLongDatatype
  test(
    "should return error when invalid Long value is processed"
  ) {
    val result =
      Column.processLongDatatype(
        "9223372036854775810", //number too large
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(
          error == "invalid_long - '9223372036854775810' Outside Long Range"
        )
      }
    }
  }

  test("should process valid Long datatype value without errors") {
    val result = Column.processLongDatatype("-1231235555", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value.toString == "-1231235555")
      }
    }
  }

  // Tests for processIntegerDatatype
  test("should process valid Integer datatype value without errors") {
    val result = Column.processIntegerDatatype("-00122", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value.toString == "-122")
      }
    }
  }

  test(
    "should return error when invalid Integer value is processed"
  ) {
    val result =
      Column.processIntegerDatatype(
        "3.0", // an integer must not contain a decimal point
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(
          error == "invalid_integer"
        )
      }
    }
  }

  // Tests for processDecimalDatatype
  test(
    "should return error when invalid Decimal value is processed"
  ) {
    val result =
      Column.processDecimalDatatype(
        "3,5", // commas are not permitted; the decimal separator must be a period
        None
      )
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(
          error == "invalid_decimal"
        )
      }
    }
  }

  test(
    "should process valid decimal datatype value beginning with decimal point without errors - "
  ) {
    val result = Column.processDecimalDatatype(".3", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value.toString == "0.3")
      }
    }
  }

  test(
    "should process valid decimal datatype value ending with decimal point without errors - "
  ) {
    val result = Column.processDecimalDatatype("3.", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value == 3)
      }
    }
  }

  //Tests for processBooleanDatatype
  test(
    "should process 1 as true for boolean datatype"
  ) {
    val result = Column.processBooleanDatatype("1", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value)
      }
    }
  }

  test(
    "should process 0 as false for boolean datatype"
  ) {
    val result = Column.processBooleanDatatype("0", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(!value)
      }
    }
  }

  test(
    "should process true as true for boolean datatype"
  ) {
    val result = Column.processBooleanDatatype("true", None)
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value)
      }
    }
  }

  test(
    "should process Y as true for boolean datatype when appropriate format is provided"
  ) {
    val result = Column.processBooleanDatatype("Y", Some("Y|N"))
    assert(result.isRight)
    result match {
      case Right(value) => {
        assert(value)
      }
    }
  }

  test(
    "should NOT process TRUE as true for boolean datatype"
  ) {
    val result =
      Column.processBooleanDatatype("TRUE", None) // values are case sensitive
    assert(result.isLeft)
    result match {
      case Left(error) => {
        assert(
          error == "invalid_boolean"
        )
      }
    }
  }
}
