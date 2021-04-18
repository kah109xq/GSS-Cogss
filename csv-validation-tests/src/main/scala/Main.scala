import sys.process._

object Main extends App {
  val path = "pwd".!!.trim
  s"$path/src/main/shell/GenerateTests.sh".!
}