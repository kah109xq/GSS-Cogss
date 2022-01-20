package CSVValidation

object PropertyType extends Enumeration {
  val Context,
      Common,
      Inherited,
      Dialect,
      Table,
      Schema,
      ForeignKey,
      Column,
      Transformation,
      ForeignKeyReference,
      Annotation,
      Undefined = Value
}