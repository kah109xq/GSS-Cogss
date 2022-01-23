package CSVValidation

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.ObjectNode
import traits.JavaIteratorExtensions.IteratorHasAsScalaArray

import java.net.URL
import java.nio.file.Paths
object Schema {
  val objectMapper = new ObjectMapper()

  def loadMetadataAndValidate(
      uri: String
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
      case e: Throwable => {
        (None, Some(s"${e.getMessage} ${e.getStackTrace}"))
      }
    }
  }

  private def fromCsvwMetadata(uri: String, json: ObjectNode): TableGroup = {
    TableGroup.fromJson(json, uri)
  }

}

case class Schema(
    uri: String,
    fields: Array[Field],
    title: JsonNode,
    description: JsonNode
) {}
