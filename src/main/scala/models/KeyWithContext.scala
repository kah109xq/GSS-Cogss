package CSVValidation
case class KeyWithContext(
    rowNumber: Long,
    keyValues: List[Any],
    var isDuplicate: Boolean = false
) {
//  def something() = {
//    val set = mutable.Set[KeyWithContext]()
//    val val1 = KeyWithContext(1, List(1,2,3))
//    val val2 = KeyWithContext(89, List(1,2,3))
//    set += val1
//    set += val2
//    assert(set.size == 1)
//  }

  override def equals(obj: Any): Boolean =
    obj != null &&
      obj.isInstanceOf[KeyWithContext] &&
      this.keyValues.equals(obj.asInstanceOf[KeyWithContext].keyValues)

  override def hashCode(): Int = this.keyValues.hashCode()

}
