package CSVValidation

import CSVValidation.traits.JavaIteratorExtensions.IteratorHasAsScalaArray
import CSVValidation.traits.ObjectNodeExtentions.IteratorHasGetKeysAndValues
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode,
  TextNode
}
import org.apache.commons.csv.CSVRecord

import java.net.URL
import scala.collection.mutable
import scala.collection.mutable.Map
import scala.util.matching.Regex

object TableGroup {
  val csvwContextUri = "http://www.w3.org/ns/csvw"
  val validProperties: Array[String] = Array[String]("tables", "notes", "@type")
  val containsWhitespaces: Regex = ".*\\s.*".r

  def fromJson(
      tableGroupNodeIn: ObjectNode,
      baseUri: String
  ): (TableGroup, WarningsAndErrors) = {
    var baseUrl = baseUri.trim
    var errors = Array[ErrorWithCsvContext]()
    var warnings = Array[WarningWithCsvContext]()
    val matcher = containsWhitespaces.pattern.matcher(baseUrl)
    if (matcher.matches()) {
      println(
        "Warning: The path/url has whitespaces in it, please ensure its correctness. Proceeding with received " +
          "path/url .."
      )
    }
    val processContextResult =
      processContextGetBaseUrlLang(tableGroupNodeIn, baseUrl, "und")
    baseUrl = processContextResult._1
    val lang = processContextResult._2
    warnings = Array.concat(warnings, processContextResult._3)
    val tableGroupNode = restructureIfNodeIsSingleTable(tableGroupNodeIn)

    val (annotations, commonProperties, inheritedProperties, w1) =
      classifyPropertiesBasedOnPropertyTypeAndSetWarnings(
        tableGroupNode,
        baseUrl,
        lang
      )
    warnings = Array.concat(warnings, w1)
    val id = getId(commonProperties)
    ensureTypeofTableGroup(tableGroupNode)

    val (tables, warningsAndErrors) = createTableObjectsAndSetWarnings(
      tableGroupNode,
      baseUrl,
      lang,
      commonProperties,
      inheritedProperties
    )
    warnings = warnings.concat(warningsAndErrors.warnings)
    errors = errors.concat(warningsAndErrors.errors)

    findForeignKeysLinkToReferencedTables(baseUrl, tables)

    val tableGroup = TableGroup(
      baseUrl,
      id,
      tables,
      commonProperties.get("notes"),
      annotations
    )
    (tableGroup, WarningsAndErrors(warnings = warnings, errors = errors))
  }

  private def restructureIfNodeIsSingleTable(
      tableGroupNode: ObjectNode
  ): ObjectNode = {
    if (tableGroupNode.path("tables").isMissingNode) {
      if (!tableGroupNode.path("url").isMissingNode) {
        val newTableGroup = JsonNodeFactory.instance.objectNode()
        val tables = JsonNodeFactory.instance.arrayNode()
        tables.insert(0, tableGroupNode)
        newTableGroup.set("tables", tables)
        return newTableGroup
      }
    }
    tableGroupNode
  }

  /**
    * This function validates the second item in context property.
    * The second element can be @language or @base - "@context": ["http://www.w3.org/ns/csvw", {"@language": "en"}]
    * @param contextBaseAndLangObject
    * @param baseUrl
    * @param lang
    * @return newBaseUrl, newLang, warnings (if any)
    */
  def getAndValidateBaseAndLangFromContextObject(
      contextBaseAndLangObject: ObjectNode,
      baseUrl: String,
      lang: String
  ): (String, String, Array[WarningWithCsvContext]) = {
    var baseUrlNew = baseUrl
    var langNew: String = lang
    var warnings = Array[WarningWithCsvContext]()

    for ((property, value) <- contextBaseAndLangObject.getKeysAndValues) {
      val (newValue, w, csvwPropertyType) = PropertyChecker
        .checkProperty(property, value, baseUrl, lang)
      if (w.isEmpty) {
        if (csvwPropertyType == PropertyType.Context) {
          property match {
            case "@base"     => baseUrlNew = newValue.asText()
            case "@language" => langNew = newValue.asText()
          }
        } else {
          throw MetadataError(
            s"@context contains properties other than @base or @language $property)"
          )
        }
      } else {
        if (!Array[String]("@base", "@language").contains(property)) {
          throw MetadataError(
            s"@context contains properties other than @base or @language $property)"
          )
        }
        warnings = warnings.concat(w.map { w =>
          WarningWithCsvContext(
            w,
            "metadata",
            "",
            "",
            s"${property}: ${value}",
            ""
          )
        })
      }
    }
    (baseUrlNew, langNew, warnings)
  }

  // https://www.w3.org/TR/2015/REC-tabular-metadata-20151217/#top-level-properties
  def validateContextArrayNode(
      context: ArrayNode,
      baseUrl: String,
      lang: String
  ): (String, String, Array[WarningWithCsvContext]) = {
    def validateFirstItemInContext(): Unit = {
      context.get(0) match {
        case s: TextNode if s.asText == csvwContextUri => {}
        case _ =>
          throw MetadataError(
            s"First item in @context must be string ${csvwContextUri} "
          )
      }
    }

    context.size() match {
      case 2 => {
        // if @context contains 2 elements, the first element will be the namespace for csvw - http://www.w3.org/ns/csvw
        // The second element can be @language or @base - "@context": ["http://www.w3.org/ns/csvw", {"@language": "en"}]
        validateFirstItemInContext()
        context.get(1) match {
          case contextBaseAndLangObject: ObjectNode =>
            getAndValidateBaseAndLangFromContextObject(
              contextBaseAndLangObject,
              baseUrl,
              lang
            )
          case _ =>
            throw new MetadataError(
              "Second @context array value must be an object"
            )
        }
      }
      case 1 => {
        // If @context contains just one element, the namespace for csvw should always be http://www.w3.org/ns/csvw
        // "@context": "http://www.w3.org/ns/csvw"
        validateFirstItemInContext()
        (baseUrl, lang, Array[WarningWithCsvContext]())
      }
      case l =>
        throw new MetadataError(s"Unexpected @context array length $l")
    }
  }

  def processContextGetBaseUrlLang(
      rootNode: ObjectNode,
      baseUrl: String,
      lang: String
  ): (String, String, Array[WarningWithCsvContext]) = {
    val context = rootNode.get("@context")

    val (baseUrlNew, langNew, warnings) = context match {
      case a: ArrayNode => validateContextArrayNode(a, baseUrl, lang)
      case s: TextNode if s.asText == csvwContextUri =>
        (baseUrl, lang, Array[WarningWithCsvContext]())
      case _ => throw MetadataError("Invalid Context")
    }
    rootNode.remove("@context")
    (baseUrlNew, langNew, warnings)
  }

  private def findForeignKeysLinkToReferencedTables(
      baseUrl: String,
      tables: mutable.Map[String, Table]
  ): Unit = {
    for ((tableUrl, table) <- tables) {
      for ((foreignKey, i) <- table.foreignKeys.zipWithIndex) {
        val reference = foreignKey.jsonObject.get("reference")
        val parentTable: Table = setReferencedTableOrThrowException(
          baseUrl,
          tables,
          tableUrl,
          i,
          reference
        )
        val mapNameToColumn: mutable.Map[String, Column] = Map()
        for (column <- parentTable.columns) {
          column.name
            .foreach(columnName => mapNameToColumn += (columnName -> column))
        }

        val parentReferencedColumns: Array[Column] = reference
          .get("columnReference")
          .elements()
          .asScalaArray
          .map(columnReference => {
            mapNameToColumn.get(columnReference.asText()) match {
              case Some(column) => column
              case None =>
                throw new MetadataError(
                  s"column named ${columnReference.asText()} does not exist in ${parentTable.url}," +
                    s" $$.tables[?(@.url = '${tableUrl}')].tableSchema.foreign_keys[${i}].reference.columnReference"
                )
            }
          })

        val foreignKeyWithTable =
          ParentTableForeignKeyReference(
            foreignKey,
            parentTable,
            parentReferencedColumns,
            table
          )
        parentTable.foreignKeyReferences :+= foreignKeyWithTable
        tables += (parentTable.url -> parentTable)
      }
    }
  }

  private def setReferencedTableOrThrowException(
      baseUrl: String,
      tables: Map[String, Table],
      tableUrl: String,
      foreignKeyArrayIndex: Int,
      reference: JsonNode
  ): Table = {
    val resourceNode = reference.path("resource")
    if (resourceNode.isMissingNode) {
      val schemaReferenceNode =
        reference.get(
          "schemaReference"
        ) // Perform more checks and provide useful error messages if schemaReference is not present
      val schemaUrl =
        new URL(new URL(baseUrl), schemaReferenceNode.asText()).toString
      val referencedTables = List.from(
        tables.values.filter(t =>
          t.schemaId.isDefined && t.schemaId.get == schemaUrl
        )
      )
      referencedTables match {
        case referencedTable :: _ => referencedTable
        case Nil =>
          throw new MetadataError(
            s"Could not find foreign key referenced schema ${schemaUrl}, " +
              s"$$.tables[?(@.url = '${tableUrl}')].tableSchema.foreignKeys[${foreignKeyArrayIndex}].reference.SchemaReference"
          )
      }
    } else {
      val referencedTableUrl = new URL(
        new URL(baseUrl),
        resourceNode.asText()
      ).toString
      tables.get(referencedTableUrl) match {
        case Some(refTable) => {
          refTable
        }
        case None =>
          throw new MetadataError(
            s"Could not find foreign key referenced table ${referencedTableUrl}, " +
              s"$$.tables[?(@.url = '${tableUrl}')].tableSchema.foreignKeys[${foreignKeyArrayIndex}].reference.resource"
          )
      }
    }
  }

  private def ensureTypeofTableGroup(tableGroupNode: ObjectNode): Unit = {
    val typeNode = tableGroupNode.path("@type")
    if (!typeNode.isMissingNode && typeNode.asText != "TableGroup") {
      throw new MetadataError(
        s"@type of table group is not 'TableGroup', found @type to be a '${typeNode.asText()}'"
      )
    }
  }

  def classifyPropertiesBasedOnPropertyTypeAndSetWarnings(
      tableGroupNode: ObjectNode,
      baseUrl: String,
      lang: String
  ): (
      Map[String, JsonNode],
      Map[String, JsonNode],
      Map[String, JsonNode],
      Array[WarningWithCsvContext]
  ) = {
    val annotations = Map[String, JsonNode]()
    val commonProperties = Map[String, JsonNode]()
    val inheritedProperties = Map[String, JsonNode]()
    var warnings = Array[WarningWithCsvContext]()
    for ((property, value) <- tableGroupNode.getKeysAndValues) {
      if (!validProperties.contains(property)) {
        val (newValue, w, csvwPropertyType) =
          PropertyChecker.checkProperty(property, value, baseUrl, lang)
        warnings = w.map(x =>
          WarningWithCsvContext(
            x,
            "metadata",
            "",
            "",
            s"$property : ${value.toPrettyString}",
            ""
          )
        )
        csvwPropertyType match {
          case PropertyType.Annotation => annotations += (property -> newValue)
          case PropertyType.Common     => commonProperties += (property -> newValue)
          case PropertyType.Inherited =>
            inheritedProperties += (property -> newValue)
          case _ => {
            warnings :+= WarningWithCsvContext(
              "invalid_property",
              "metadata",
              "",
              "",
              property,
              ""
            )
          }
        }
      }
      ()
    }
    (annotations, commonProperties, inheritedProperties, warnings)
  }

  private def createTableObjectsAndSetWarnings(
      tableGroupNode: ObjectNode,
      baseUrl: String,
      lang: String,
      commonProperties: mutable.Map[String, JsonNode],
      inheritedProperties: mutable.Map[String, JsonNode]
  ): (mutable.Map[String, Table], WarningsAndErrors) = {
    tableGroupNode.path("tables") match {
      case t: ArrayNode if t.isEmpty() =>
        throw MetadataError("Empty tables property")
      case t: ArrayNode =>
        extractTablesFromNode(
          t,
          baseUrl,
          lang,
          commonProperties,
          inheritedProperties
        )
      case n if n.isMissingNode => throw MetadataError("No tables property")
      case _                    => throw MetadataError("Tables property is not an array")
    }
  }

  def extractTablesFromNode(
      tablesArrayNode: ArrayNode,
      baseUrl: String,
      lang: String,
      commonProperties: Map[String, JsonNode],
      inheritedProperties: Map[String, JsonNode]
  ): (mutable.Map[String, Table], WarningsAndErrors) = {
    var warnings = Array[WarningWithCsvContext]()
    var errors = Array[ErrorWithCsvContext]()
    val tables = Map[String, Table]()
    for (tableElement <- tablesArrayNode.elements().asScalaArray) {
      tableElement match {
        case tableElementObject: ObjectNode => {
          var tableUrl = tableElement.get("url")
          if (!tableUrl.isTextual) {
            errors = errors :+ ErrorWithCsvContext(
              "invalid_url",
              "metadata",
              "",
              "",
              s"url: $tableUrl",
              ""
            )
            tableUrl = new TextNode("")
          }
          tableUrl = new TextNode(
            new URL(new URL(baseUrl), tableUrl.asText()).toString
          )
          tableElementObject.set("url", tableUrl)
          val (table, w) = Table.fromJson(
            tableElementObject,
            baseUrl,
            lang,
            commonProperties,
            inheritedProperties
          )
          tables += (tableUrl.asText -> table)
          warnings = warnings.concat(w)
        }
        case _ => {
          warnings = warnings :+ WarningWithCsvContext(
            "invalid_table_description",
            "metadata",
            "",
            "",
            s"Value must be instance of object, found: ${tableElement.toPrettyString}",
            ""
          )
        }
      }
    }
    (tables, WarningsAndErrors(errors = errors, warnings = warnings))
  }

  private def getId(commonProperties: Map[String, JsonNode]) = {
    commonProperties
      .get("@id")
      .map(idNode => idNode.asText())
  }

}
case class TableGroup private (
    baseUrl: String,
    id: Option[String],
    tables: Map[String, Table],
    notes: Option[JsonNode],
    annotations: Map[String, JsonNode]
) {

  def validateHeader(
      header: CSVRecord,
      tableUrl: String
  ): WarningsAndErrors = tables(tableUrl).validateHeader(header)
}
