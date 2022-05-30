package CSVValidation

case class ParentTableForeignKeyReference(
    foreignKey: ChildTableForeignKey,
    parentTable: Table,
    parentTableReferencedColumns: Array[Column],
    childTable: Table
) {}
