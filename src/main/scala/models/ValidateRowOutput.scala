package CSVValidation

case class ValidateRowOutput(
    warningsAndErrors: WarningsAndErrors,
    primaryKeyValues: List[Any],
    parentTableForeignKeyReferences: Map[
      ParentTableForeignKeyReference,
      KeyWithContext
    ],
    childTableForeignKeys: Map[ChildTableForeignKey, KeyWithContext]
)
