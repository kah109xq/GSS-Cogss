name := "csv-validation-new"

organization := "ONS"
version := "0.1"
scalaVersion := "2.13.4"

autoCompilerPlugins := true

// Want to use a published library in your project?
// You can define other libraries as dependencies in your build like this:

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

libraryDependencies += "io.cucumber" %% "cucumber-scala" % "6.8.2" % Test
libraryDependencies += "io.cucumber" % "cucumber-junit" % "6.8.2" % Test
libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.5"
libraryDependencies += "org.apache.jena" % "jena-arq" % "3.14.0"
libraryDependencies += "joda-time" % "joda-time" % "2.10.8"
libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.10"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.1"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.12.1"
libraryDependencies += "com.fasterxml.jackson.core" %"jackson-core" % "2.12.1"


// Here, `libraryDependencies` is a set of dependencies, and by using `+=`,
// we're adding the scala-parser-combinators dependency to the set of dependencies
// that sbt will go and fetch when it starts up.
// Now, in any Scala file, you can import classes, objects, etc., from
// scala-parser-combinators with a regular import.

// TIP: To find the "dependency" that you need to add to the
// `libraryDependencies` set, which in the above example looks like this:

// "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

// You can use Scaladex, an index of all known published Scala libraries. There,
// after you find the library you want, you can just copy/paste the dependency
// information that you need into your build file. For example, on the
// scala/scala-parser-combinators Scaladex page,
// https://index.scala-lang.org/scala/scala-parser-combinators, you can copy/paste
// the sbt dependency from the sbt box on the right-hand side of the screen.

// IMPORTANT NOTE: while build files look _kind of_ like regular Scala, it's
// important to note that syntax in *.sbt files doesn't always behave like
// regular Scala. For example, notice in this build file that it's not required
// to put our settings into an enclosing object or class. Always remember that
// sbt is a bit different, semantically, than vanilla Scala.

// ============================================================================

// Most moderately interesting Scala projects don't make use of the very simple
// build file style (called "bare style") used in this build.sbt file. Most
// intermediate Scala projects make use of so-called "multi-project" builds. A
// multi-project build makes it possible to have different folders which sbt can
// be configured differently for. That is, you may wish to have different
// dependencies or different testing frameworks defined for different parts of
// your codebase. Multi-project builds make this possible.

// Here's a quick glimpse of what a multi-project build looks like for this
// build, with only one "subproject" defined, called `root`:

// lazy val root = (project in file(".")).
//   settings(
//     inThisBuild(List(
//       organization := "ch.epfl.scala",
//       scalaVersion := "2.13.1"
//     )),
//     name := "hello-world"
//   )

// To learn more about multi-project builds, head over to the official sbt
// documentation at http://www.scala-sbt.org/documentation.html

Compile / resourceGenerators += Def.task {
  import sys.process._
  val path = "pwd".!!.trim
  s"$path/src/main/ruby/GenerateTests.rb".!
  val file = (Compile / resourceManaged).value/".."/".."/".."/".."/"src"/"test"/"resources"/"features"/"csvw_validation_tests.feature"
  Seq(file)
}.taskValue