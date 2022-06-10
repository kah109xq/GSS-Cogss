package CSVValidation

case class ValidateRowOutput(
    warningsAndErrors: WarningsAndErrors = WarningsAndErrors(),
    primaryKeyValues: List[Any] = List(),
    parentTableForeignKeyReferences: Map[
      ParentTableForeignKeyReference,
      KeyWithContext
    ] = Map(),
    childTableForeignKeys: Map[ChildTableForeignKey, KeyWithContext] = Map()
)
