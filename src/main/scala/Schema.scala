package CSVValidation

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.ObjectNode
import traits.JavaIteratorExtensions.IteratorHasAsScalaArray

import java.net.URL
import java.nio.file.Paths
object Schema {
  val objectMapper = new ObjectMapper()

  def loadMetadataAndValidate(
      uri: String,
      outputErrors: Boolean = true
  ): (Option[TableGroup], Option[String]) = {
    try {
      val jsonNode = objectMapper.readTree(Paths.get(uri).toFile)
      (
        Some(
          Schema
            .fromCsvwMetadata(s"file:$uri", jsonNode.asInstanceOf[ObjectNode])
        ),
        None
      )
    } catch {
      case metadataError: MetadataError => {
        (None, Some(metadataError.getMessage))
      }
      case _: Throwable => {
        (None, Some("Unknown Exception occurred"))
      }
    }
  }

  private def fromCsvwMetadata(uri: String, json: ObjectNode): TableGroup = {
    TableGroup.fromJson(json, uri)
  }

  // Not in use now
  private def fromJsonTable(uri: String, json: ObjectNode): Schema = {
    var fields = Array[Field]()
    val fieldsNode = json.path("fields")
    if (!fieldsNode.isMissingNode) {
      for (fieldDesc <- fieldsNode.elements().asScalaArray) {
        fields :+= Field(
          Some(fieldDesc.get("name")),
          Some(fieldDesc.get("constraints")),
          Some(fieldDesc.get("title")),
          Some(fieldDesc.get("description"))
        )
      }
    }
    Schema(uri, fields, json.get("title"), json.get("description"))
  }
}

case class Schema(
    uri: String,
    fields: Array[Field],
    title: JsonNode,
    description: JsonNode
) {}
