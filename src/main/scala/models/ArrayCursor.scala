package models

import scala.reflect.ClassTag

case class ArrayCursor[T: ClassTag](private val values: Seq[T]) {
  val arrayValues: Array[T] = Array.from[T](values)
  private var currentIndex: Int = -1

  /**
    * Whether or not the Array has a value `offset` items relative to the current index.
    * @param offset
    * @return
    */
  def hasValue(offset: Int) = {
    val offsetPosition = currentIndex + offset
    val maxIndex = arrayValues.length - 1
    maxIndex >= 0 && offsetPosition >= 0 && offsetPosition <= maxIndex
  }

  def hasNext() = hasValue(1)
  def hasPrevious() = hasValue(-1)

  /**
    * Set the index to `currentIndex + 1`
    * @return the next value
    */
  def next(): T = {
    currentIndex += 1
    arrayValues(currentIndex)
  }

  /**
    * Set the index to `currentIndex - 1`
    */
  def stepBack(): Unit = currentIndex -= 1

  def peek(offset: Int): T = arrayValues(currentIndex + offset)

  def peekNext(): T = peek(1)

  def peekPrevious(): T = peek(-1)
}
