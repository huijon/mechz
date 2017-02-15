name := "mechz"

version := "1.0"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
)

libraryDependencies ++= Seq(
  //"org.scalatest" % "scalatest_2.10" % "2.0" % "test",
  "org.scalaz" %%  "scalaz-core" % "7.1.5"
)
//"org.scalaz" %% "scalaz-core" % "7.2.8"