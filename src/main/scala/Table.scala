package typeDefinitions
case class Table(
                columns: List[Column],
                tableDirection: String,
                foreignKeys: List[String], // revisit, not clear (a list of foreign keys on the table, as defined in [tabular-metadata], which may be an empty list.)
                id: String, // Int?
                notes: String, // revisit
                rows: List[Row],
                schema: String,
                suppressOutput: Boolean,
                transformations: String, // revisit, (a (possibly empty) list of specifications for converting this table into other formats, as defined in [tabular-metadata].)
                url: String)
