package CSVValidation
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}

object Json {
  def deepCopy(jsonParserObj: JsonParser): ObjectNode = {
    val objectMapper = new ObjectMapper()
    val node = objectMapper.readTree[ObjectNode](jsonParserObj)
    return node.deepCopy()
  }

}
