organization := "pw.yurchenko"

name := "price-list-telegram-bot"

version := "0.1"

scalaVersion := "2.12.3"

libraryDependencies ++= Seq(
  "org.telegram" % "telegrambots" % "3.2",
  "com.typesafe" % "config" % "1.3.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "org.slf4j" % "slf4j-jdk14" % "1.7.25"
)

enablePlugins(JavaAppPackaging)

name in Universal := name.value

packageName in Universal := packageName.value

mappings in Universal in packageBin += file("src/main/resources/application.conf") -> "conf/application.conf"
mappings in Universal in packageBin += file("src/main/resources/logging.properties") -> "conf/logging.properties"
mappings in Universal in packageBin += file("src/main/resources/prices/0") -> "prices/0"
mappings in Universal in packageBin += file("src/main/resources/price-list-telegram-bot.service") -> "bin/price-list-telegram-bot.service"

bashScriptExtraDefines ++= Seq(
  """addJava "-Dconfig.file=${app_home}/../conf/application.conf" """,
  """addJava "-Djava.util.logging.config.file=${app_home}/../conf/logging.properties" """
)

batScriptExtraDefines ++= Seq(
  """set _JAVA_OPTS=%_JAVA_OPTS% -Dconfig.file=%APP_LIB_DIR%/../conf/application.conf""",
  """set _JAVA_OPTS=%_JAVA_OPTS% -Djava.util.logging.config.file=%APP_LIB_DIR%/../conf/logging.properties"""
)
