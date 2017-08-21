package ruby.sources

import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

object ConfigFile {

  val conf: Config = ConfigFactory.load()

  // Message block
  val startHeader: String = conf.getString("messages.start.header")
  val contactsHeader: String = conf.getString("messages.contacts.header")
  val commandsHeader: String = conf.getString("messages.commands.header")
  val contacts: List[String] = conf.getStringList("messages.contacts.list").asScala.toList
  val badCommand:String = conf.getString("messages.bad.command")

  //Commands
  val commands: List[String] = conf.getStringList("commands.list").asScala.toList
}
