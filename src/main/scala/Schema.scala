package CSVValidation

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.ObjectNode
import java.io.PrintWriter
import java.io.StringWriter
import traits.JavaIteratorExtensions.IteratorHasAsScalaArray

import java.net.URL
import java.nio.file.Paths
object Schema {
  val objectMapper = new ObjectMapper()

  def loadMetadataAndValidate(
      filePath: String
  ): Either[String, TableGroup] = {
    try {
      val jsonNode = objectMapper.readTree(Paths.get(filePath).toFile)
      Right(
        Schema.fromCsvwMetadata(
          s"file:$filePath",
          jsonNode.asInstanceOf[ObjectNode]
        )
      )
    } catch {
      case metadataError: MetadataError => {
        Left(metadataError.getMessage)
      }
      case e: Throwable => {
        val sw = new StringWriter
        e.printStackTrace(new PrintWriter(sw))
        Left(sw.toString)
      }
    }
  }

  private def fromCsvwMetadata(uri: String, json: ObjectNode): TableGroup = {
    TableGroup.fromJson(json, uri)
  }

}

case class Schema(
    uri: String,
    title: JsonNode,
    description: JsonNode
) {}
