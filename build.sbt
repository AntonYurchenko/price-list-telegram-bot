name := "ruby-telegram-bot"

version := "0.1"

scalaVersion := "2.12.3"

val telegramBotApiVer = "3.2"
val configVer = "1.3.1"

libraryDependencies ++= Seq(
  "org.telegram" % "telegrambots" % telegramBotApiVer,
  "com.typesafe" % "config" % configVer
)
        