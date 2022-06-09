package CSVValidation

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.fasterxml.jackson.databind.node.ObjectNode

import java.io.{File, PrintWriter, StringWriter}
import java.net.URI
import java.nio.file.Paths
object Schema {
  val objectMapper = new ObjectMapper()

  def loadMetadataAndValidate(
      schemaUri: URI
  ): Either[String, (TableGroup, Array[ErrorWithCsvContext])] = {
    try {
      val jsonNode = if (schemaUri.getScheme == "file") {
        val f = new File(schemaUri)
        objectMapper.readTree(f)
      } else {
        objectMapper.readTree(schemaUri.toURL)
      }
      Right(
        Schema.fromCsvwMetadata(
          schemaUri.toString,
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

  private def fromCsvwMetadata(
      uri: String,
      json: ObjectNode
  ): (TableGroup, Array[ErrorWithCsvContext]) = {
    TableGroup.fromJson(json, uri)
  }

}

case class Schema(
    uri: String,
    title: JsonNode,
    description: JsonNode
) {}
