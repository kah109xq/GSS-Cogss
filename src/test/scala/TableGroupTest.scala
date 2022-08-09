package CSVValidation
import CSVValidation.ConfiguredObjectMapper.objectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.{
  ArrayNode,
  JsonNodeFactory,
  ObjectNode
}
import org.scalatest.FunSuite

class TableGroupTest extends FunSuite {
  test("should create table group object from pre parsed metadata") {
    val json =
      """
        |{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "url": "countries.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "countryCode",
        |        "titles": "countryCode",
        |        "datatype": "string",
        |        "propertyUrl": "http://www.geonames.org/ontology{#_name}"
        |      }, {
        |        "name": "latitude",
        |        "titles": "latitude",
        |        "datatype": "number"
        |      }, {
        |        "name": "longitude",
        |        "titles": "longitude",
        |        "datatype": "number"
        |      }, {
        |        "name": "name",
        |        "titles": "name",
        |        "datatype": "string"
        |      }],
        |      "aboutUrl": "http://example.org/countries.csv{#countryCode}",
        |      "propertyUrl": "http://schema.org/{_name}",
        |      "primaryKey": "countryCode"
        |    }
        |  }, {
        |    "url": "country_slice.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "countryRef",
        |        "titles": "countryRef",
        |        "valueUrl": "http://example.org/countries.csv{#countryRef}"
        |      }, {
        |        "name": "year",
        |        "titles": "year",
        |        "datatype": "gYear"
        |      }, {
        |        "name": "population",
        |        "titles": "population",
        |        "datatype": "integer"
        |      }],
        |      "foreignKeys": [{
        |        "columnReference": "countryRef",
        |        "reference": {
        |          "resource": "countries.csv",
        |          "columnReference": "countryCode"
        |        }
        |      }]
        |    }
        |  }]
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (tableGroup, warnings) = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/countries.json"
    )
    val table2 =
      tableGroup.tables("http://w3c.github.io/csvw/tests/country_slice.csv")
    assert(tableGroup.id.isEmpty)
    assert(tableGroup.tables.size === 2)
    assert(tableGroup.annotations.size === 0)
    val referencedTable =
      tableGroup.tables("http://w3c.github.io/csvw/tests/countries.csv")
    assert(referencedTable.foreignKeyReferences.length === 1)
    val foreignKeyReference = referencedTable.foreignKeyReferences(0)
    assert(
      foreignKeyReference.parentTable.url === "http://w3c.github.io/csvw/tests/countries.csv"
    )

    assert(foreignKeyReference.parentTableReferencedColumns.length === 1)
    assert(
      foreignKeyReference
        .parentTableReferencedColumns(0)
        .name
        .get === "countryCode"
    )
    assert(foreignKeyReference.foreignKey.localColumns.length === 1)
    assert(
      foreignKeyReference.foreignKey
        .localColumns(0)
        .name
        .get === "countryRef"
    )
  }

  test(
    "should set foreign key references correctly when using schemaReference instead of resource"
  ) {
    val json =
      """
        |{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "tables": [{
        |    "url": "countries.csv",
        |    "tableSchema": {
        |      "@id": "http://w3c.github.io/csvw/tests/countries.json",
        |      "columns": [{
        |        "name": "countryCode",
        |        "titles": "countryCode",
        |        "datatype": "string",
        |        "propertyUrl": "http://www.geonames.org/ontology{#_name}"
        |      }, {
        |        "name": "latitude",
        |        "titles": "latitude",
        |        "datatype": "number"
        |      }, {
        |        "name": "longitude",
        |        "titles": "longitude",
        |        "datatype": "number"
        |      }, {
        |        "name": "name",
        |        "titles": "name",
        |        "datatype": "string"
        |      }],
        |      "aboutUrl": "http://example.org/countries.csv{#countryCode}",
        |      "propertyUrl": "http://schema.org/{_name}",
        |      "primaryKey": "countryCode"
        |    }
        |  }, {
        |    "url": "country_slice.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "countryRef",
        |        "titles": "countryRef",
        |        "valueUrl": "http://example.org/countries.csv{#countryRef}"
        |      }, {
        |        "name": "year",
        |        "titles": "year",
        |        "datatype": "gYear"
        |      }, {
        |        "name": "population",
        |        "titles": "population",
        |        "datatype": "integer"
        |      }],
        |      "foreignKeys": [{
        |        "columnReference": "countryRef",
        |        "reference": {
        |          "schemaReference": "countries.json",
        |          "columnReference": "countryCode"
        |        }
        |      }]
        |    }
        |  }]
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (tableGroup, warnings) = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/"
    )
    val referencedTable =
      tableGroup.tables("http://w3c.github.io/csvw/tests/countries.csv")

    assert(referencedTable.foreignKeyReferences.length === 1)
    val foreignKeyReference = referencedTable.foreignKeyReferences(0)
    assert(foreignKeyReference.parentTable.url === referencedTable.url)
    assert(foreignKeyReference.parentTableReferencedColumns.length === 1)
    assert(
      foreignKeyReference
        .parentTableReferencedColumns(0)
        .name
        .get === "countryCode"
    )
    assert(foreignKeyReference.foreignKey.localColumns.length === 1)
    assert(
      foreignKeyReference.foreignKey
        .localColumns(0)
        .name
        .get === "countryRef"
    )

  }
  test("should inherit null to all columns") {
    val json =
      """
        |{
        |  "@context": "http://www.w3.org/ns/csvw",
        |  "null": "-",
        |  "tables": [{
        |    "url": "test040.csv",
        |    "tableSchema": {
        |      "columns": [{
        |        "titles": "null"
        |      }, {
        |        "titles": "lang"
        |      }, {
        |        "titles": "textDirection"
        |      }, {
        |        "titles": "separator"
        |      }, {
        |        "titles": "ordered"
        |      }, {
        |        "titles": "default"
        |      }, {
        |        "titles": "datatype"
        |      }, {
        |        "titles": "aboutUrl"
        |      }, {
        |        "titles": "propertyUrl"
        |      }, {
        |        "titles": "valueUrl"
        |      }]
        |    }
        |  }]
        |}
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (tableGroup, warningsAndErrors) = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/test040-metadata.json"
    )
    val table =
      tableGroup.tables("http://w3c.github.io/csvw/tests/test040.csv")

    assert(tableGroup.annotations.size === 0)
    assert(warningsAndErrors.warnings.length === 0)
    assert(tableGroup.tables.size === 1)
    assert(table.columns.length === 10)
    assert(
      table.columns(0).nullParam === Array("-")
    ) // should inherit null to all columns - assertion which justifies test name
  }

  test(
    "should initialize TableGroup object correctly when tables key is not present (just one table)"
  ) {
    val json =
      """
        |{
        |    "@context": ["http://www.w3.org/ns/csvw", {"@language": "en"}],
        |    "url": "tree-ops.csv",
        |    "dc:title": "Tree Operations",
        |    "dcat:keyword": ["tree", "street", "maintenance"],
        |    "dc:publisher": {
        |      "schema:name": "Example Municipality",
        |      "schema:url": {"@id": "http://example.org"}
        |    },
        |    "dc:license": {"@id": "http://opendefinition.org/licenses/cc-by/"},
        |    "dc:modified": {"@value": "2010-12-31", "@type": "xsd:date"},
        |    "tableSchema": {
        |      "columns": [{
        |        "name": "GID",
        |        "titles": ["GID", "Generic Identifier"],
        |        "dc:description": "An identifier for the operation on a tree.",
        |        "datatype": "string",
        |        "required": true
        |      }, {
        |        "name": "on_street",
        |        "titles": "On Street",
        |        "dc:description": "The street that the tree is on.",
        |        "datatype": "string"
        |      }, {
        |        "name": "species",
        |        "titles": "Species",
        |        "dc:description": "The species of the tree.",
        |        "datatype": "string"
        |      }, {
        |        "name": "trim_cycle",
        |        "titles": "Trim Cycle",
        |        "dc:description": "The operation performed on the tree.",
        |        "datatype": "string"
        |      }, {
        |        "name": "inventory_date",
        |        "titles": "Inventory Date",
        |        "dc:description": "The date of the operation that was performed.",
        |        "datatype": {"base": "date", "format": "M/d/yyyy"}
        |      }],
        |      "primaryKey": "GID",
        |      "aboutUrl": "#gid-{GID}"
        |    }
        |  }
        |""".stripMargin
    val jsonNode = objectMapper.readTree(json)
    val (tableGroup, warnings) = TableGroup.fromJson(
      jsonNode.asInstanceOf[ObjectNode],
      "http://w3c.github.io/csvw/tests/test040-metadata.json"
    )
    val table =
      tableGroup.tables("http://w3c.github.io/csvw/tests/tree-ops.csv")

    assert(tableGroup.tables.size === 1)
    assert(table.columns.length === 5)
  }

  test(
    "should raise exception for invalid second element in context array"
  ) {
    val json =
      """
          |[
          | "http://www.w3.org/ns/csvw",
          | [
          |   "fr",
          |   "http://new-base-url"
          | ]
          |]
          |
          |""".stripMargin
    val arrayNode = objectMapper
      .readTree(json)
      .asInstanceOf[ArrayNode]
    val thrown = intercept[MetadataError] {
      TableGroup.validateContextArrayNode(
        arrayNode,
        "http://default-base-url",
        "default-lang"
      )
    }
    assert(
      thrown.getMessage === "Second @context array value must be an object"
    )
  }

  test(
    "should raise exception if first element of context array is not valid"
  ) {
    val json =
      """
        |[
        | "http://invalid-context-uri.com",
        | [
        |   "fr",
        |   "http://new-base-url"
        | ]
        |]
        |
        |""".stripMargin
    val arrayNode = objectMapper
      .readTree(json)
      .asInstanceOf[ArrayNode]
    val thrown = intercept[MetadataError] {
      TableGroup.validateContextArrayNode(
        arrayNode,
        "http://default-base-url",
        "default-lang"
      )
    }
    assert(
      thrown.getMessage === "First item in @context must be string http://www.w3.org/ns/csvw "
    )
  }

  test("ensure baseUrl and lang can be correctly extracted from @context") {

    val json =
      """
        |[
        | "http://www.w3.org/ns/csvw", 
        | {
        |   "@language": "fr",
        |   "@base": "http://new-base-url"
        | }
        |]
        |
        |""".stripMargin
    val arrayNode = objectMapper
      .readTree(json)
      .asInstanceOf[ArrayNode]
    val (newBaseUrl, newLang, err) = TableGroup.validateContextArrayNode(
      arrayNode,
      "http://default-base-url",
      "default-lang"
    )
    assert(newBaseUrl === "http://new-base-url")
    assert(newLang === "fr")
    assert(err.length === 0)
  }

}
