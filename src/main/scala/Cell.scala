package typeDefinitions
case class Cell(
               aboutUrl: String,
               column: Column,
               errors: List[String], // revisit! Lists in scala are immutable, if errors need to be updated this will not work
               ordered: Boolean,
               propertyUrl: String,
               row: Row,
               stringValue: String,
               table: Table,
               textDirection: String,
               value: String,
               valueUrl: String)
