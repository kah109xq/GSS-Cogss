package typeDefinitions
case class Column(
                 aboutUrl: String,
                 cells: List[Cell],
                 dataType: String, // revisit, (the expected datatype for the values of cells in this column, as defined in [tabular-metadata])
                 default: String,
                 lang: String,
                 name: String,
                 `null`: List[String],
                 number: Int, // BigInt?
                 ordered: Boolean,
                 propertyUrl: String,
                 required: Boolean,
                 separator: String,
                 sourceNumber: Int, //BigInt?
                 suppressOutput: Boolean,
                 table: Table,
                 textDirection: String,
                 titles: List[String], // revisit
                 valueUrl: String,
                 virtual: Boolean
                 )