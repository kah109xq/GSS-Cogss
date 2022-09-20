package models

import scala.reflect.ClassTag

case class ArrayCursor[T: ClassTag](private val values: Seq[T]) {
  val arrayValues: Array[T] = Array.from[T](values)
  private var currentPosition: Int = -1

  def hasNext() = currentPosition + 1 <= arrayValues.length - 1
  def hasPrevious() = currentPosition > 0

  /**
    * Set the pointer to `currentPosition + 1`
    * @return the next value
    */
  def next(): T = {
    currentPosition += 1
    arrayValues(currentPosition)
  }

  /**
    * Set the pointer to `currentPosition - 1`
    */
  def stepBack(): Unit = currentPosition -= 1

  def peekNext(): T = arrayValues(currentPosition + 1)

  def peekPrevious(): T = arrayValues(currentPosition - 1)
}
