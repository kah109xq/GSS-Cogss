package CSVValidation
import com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.FunSuite

class JsonTest extends FunSuite {

  test("test deep copy works with dictionary") {
    val thingToCopy =
      """
        | {
        |   "x": "Hello",
        |   "y": "world"
        | }
        |""".stripMargin
    val objectMapper = new ObjectMapper()
    val jsonParserObj = objectMapper.createParser(thingToCopy)
    val copiedObject = Json.deepCopy(jsonParserObj)
    assert(copiedObject.get("x").asText === "Hello")
  }

}
