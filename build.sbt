name := "mechz"

version := "1.0"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "org.scalaz"                  %%  "scalaz-core"                   % "7.1.5",
  "org.http4s"                  %% "http4s-blaze-server"            % "0.12.3" withSources(),
  "org.http4s"                  %% "http4s-dsl"                     % "0.12.3" withSources(),
  "org.http4s"                  %% "http4s-argonaut"                % "0.12.3" withSources(),
  "com.typesafe"                % "config"                          % "1.3.0",
  "ch.qos.logback"              % "logback-classic"                 % "1.1.3",
  "com.typesafe.scala-logging"  %% "scala-logging"                  % "3.1.0",
  "com.logentries"              % "logentries-appender"             % "1.1.14",
  "io.argonaut"                 %% "argonaut"                       % "6.1"
)