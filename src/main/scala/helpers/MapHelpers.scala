package CSVValidation

import com.fasterxml.jackson.databind.JsonNode
import scala.collection.mutable.Map

object MapHelpers {
  def deepCloneJsonPropertiesMap(
      propertiesMap: Map[String, JsonNode]
  ): Map[String, JsonNode] = {
    Map.from(
      propertiesMap.toArray.map({
        case (propertyName: String, value: JsonNode) =>
          (propertyName, value.deepCopy())
      })
    )
  }
}
