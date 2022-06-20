package CSVValidation

import CSVValidation.traits.ObjectNodeExtentions.ObjectNodeGetMaybeNode
import CSVValidation.traits.JavaIteratorExtensions.IteratorHasAsScalaArray

import com.fasterxml.jackson.databind.node.{ArrayNode, ObjectNode, TextNode}

object Dialect {

  def fromJson(dialectNode: ObjectNode): Dialect = {
    val encoding = dialectNode
      .getMaybeNode("encoding")
      .map(n => n.asText())
      .getOrElse("UTF-8")

    val lineTerminators = dialectNode
      .getMaybeNode("lineTerminator")
      .map {
        case n: TextNode  => Array(n.asText())
        case n: ArrayNode => n.iterator().asScalaArray.map(_.asText())
        case n =>
          throw MetadataError(s"Unexpected node type ${n.getClass.getName}")
      }
      .getOrElse(Array("\n", "\r\n"))

    val quoteChar = dialectNode
      .getMaybeNode("quoteChar")
      .map(_.asText.charAt(0))
      .getOrElse('\"')

    val doubleQuote = dialectNode
      .getMaybeNode("doubleQuote")
      .map(_.asBoolean())
      .getOrElse(true)

    val commentPrefix = dialectNode
      .getMaybeNode("commentPrefix")
      .map(_.asText())
      .getOrElse("#")

    val header =
      dialectNode.getMaybeNode("header").map(_.asBoolean()).getOrElse(true)

    val headerRowCount =
      dialectNode.getMaybeNode("headerRowCount").map(_.asInt).getOrElse(1)

    val skipRows =
      dialectNode.getMaybeNode("skipRows").map(_.asInt()).getOrElse(0)

    val delimiter = dialectNode
      .getMaybeNode("delimiter")
      .map(_.asText().charAt(0))
      .getOrElse(',')

    val skipColumns =
      dialectNode.getMaybeNode("skipColumns").map(_.asInt()).getOrElse(0)

    val skipBlankRows = dialectNode
      .getMaybeNode("skipBlankRows")
      .map(_.asBoolean())
      .getOrElse(false)

    val skipInitialSpace = dialectNode
      .getMaybeNode("skipInitialSpace")
      .map(_.asBoolean)
      .getOrElse(false)
    val trim = dialectNode
      .getMaybeNode("trim")
      .map(n => TrimType.fromString(n.asText))
      .getOrElse(TrimType.False)

    Dialect(
      encoding,
      lineTerminators,
      quoteChar,
      doubleQuote,
      skipRows,
      commentPrefix,
      header,
      headerRowCount,
      delimiter,
      skipColumns,
      skipBlankRows,
      skipInitialSpace,
      trim
    )
  }
}

case class Dialect(
    encoding: String = "UTF-8",
    lineTerminators: Array[String] = Array("\n", "\r\n"),
    quoteChar: Char = '\"',
    doubleQuote: Boolean = true,
    skipRows: Int = 0,
    commentPrefix: String = "#",
    header: Boolean = true,
    headerRowCount: Int = 1,
    delimiter: Char = ',',
    skipColumns: Int = 0,
    skipBlankRows: Boolean = false,
    skipInitialSpace: Boolean = false,
    trim: TrimType.Value = TrimType.True
)
