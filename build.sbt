organization := "pw.yurchenko"

name := "ruby-telegram-bot"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.telegram" % "telegrambots" % "3.2",
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.slf4j" % "slf4j-simple" % "1.7.25"
)

enablePlugins(JavaServerAppPackaging)
enablePlugins(UniversalPlugin)

name in Universal := name.value

packageName in Universal := packageName.value
        