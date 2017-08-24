package pw.yurchenko.sources

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters._

/**
  * The object gives access to parameters from application.conf file
  *
  * @author Anton Yurchenko
  */
object ConfigFile {

  val conf: Config = ConfigFactory.load()

  // Telegram
  val botUserName: String = conf.getString("telegram.bot.user.name")
  val botToken: String = conf.getString("telegram.bot.token")

  // Message block
  val startHeader: String = conf.getString("messages.start.header")
  val contactsHeader: String = conf.getString("messages.contacts.header")
  val commandsHeader: String = conf.getString("messages.commands.header")
  val contacts: List[String] = conf.getStringList("messages.contacts.list").asScala.toList
  val badCommand: String = conf.getString("messages.bad.command")
  val priceNotAvailable: String = conf.getString("messages.price.not.available")
  val adminWelcome: String = conf.getString("messages.admin.welcome")
  val successStatus: String = conf.getString("messages.success.status")

  //Commands
  val commands: List[String] = conf.getStringList("commands.list").asScala.toList

  //Price save
  val priceDir: File = new File(conf.getString("price.save.dir"))

  // Admin password
  val adminPassword: String = conf.getString("admin.password")
  val adminPeriod: Long = conf.getInt("admin.auto.disable") * 60L * 1000L
}
