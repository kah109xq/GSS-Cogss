package typeDefinitions
case class Row(
              cells: List[Cell],
              number: Int, // BigInt ?
              primaryKey: List[Cell], // revisit! (a possibly empty list of cells whose values together provide a unique identifier for this row. This is similar to the name of a column)
              titles: List[String],
              referencedRows: List[String], // revist! (a possibly empty list of pairs of a foreign key and a row in a table within the same group of tables (which may be another row in the table in which this row appears))
              sourceNumber: Int,
              table: Table)