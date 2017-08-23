package ruby.handlers

import java.io.File

import com.typesafe.scalalogging.LazyLogging
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ruby.entiny.AdminStatus
import ruby.sources.ConfigFile._
import ruby.sources.PriceSource

/**
  * The class is handler of commands for PriceBot
  *
  * @author Anton Yurchenko
  */
class PriceBotCommandHandler extends CommandHandler with LazyLogging {

  private var admin = AdminStatus()

  /**
    * The method is handling message from chat with command
    *
    * @param msgWithCmd Message taken message
    * @return Seq[SendMessage]
    */
  override def handle(msgWithCmd: Message): Seq[SendMessage] = {

    val chatId = msgWithCmd.getChatId
    val userName = if (msgWithCmd.getChat.getUserName == null) "Empty" else msgWithCmd.getChat.getUserName
    val userFirstName = if (msgWithCmd.getChat.getFirstName == null) "Empty" else msgWithCmd.getChat.getFirstName
    val userLastName = if (msgWithCmd.getChat.getLastName == null) "Empty" else msgWithCmd.getChat.getLastName
    val msgText = msgWithCmd.getText

    msgText match {

      // Bot sends start welcome message
      case "/start" =>
        val text = (startHeader :: commandsHeader :: commands).mkString("\n")
        logger.info(s"User: $userFirstName $userLastName with userName:$userName has been connected to $botUserName (chatId:$chatId)")
        new SendMessage(chatId, text).enableHtml(true) :: Nil

      // Bot sends message with contacts
      case "/contact" =>
        val text = (contactsHeader :: contacts).mkString("\n")
        logger.info(s"$botUserName has sent contact information to user with userName:$userName (chatId:$chatId)")
        new SendMessage(chatId, text).enableHtml(true) :: Nil

      // Bot sends message with information about available commands
      case "/help" =>
        val text = (commandsHeader :: commands).mkString("\n")
        logger.info(s"$botUserName has sent help information to user with userName:$userName (chatId:$chatId)")
        new SendMessage(chatId, text).enableHtml(true) :: Nil

      // Bot sends message with price list
      case "/price" =>
        val price = PriceSource.readActual(priceDir)
        if (price.isEmpty) new SendMessage(chatId, priceNotAvailable) :: Nil
        else {
          logger.info(s"$botUserName has sent price list to user with userName:$userName (chatId:$chatId)")
          price.map(_.mkString("\n"))
            .map(text => new SendMessage(chatId, text))
        }

      //Bot enables password standby
      case "/admin" =>
        admin = AdminStatus(waitPass = true)
        logger.info(s"User with userId:$userName enable password standby (chatId:$chatId)")
        new SendMessage(chatId, badCommand).enableHtml(true) :: Nil

      // Bot disables admin mode by time out
      case _ if admin.isAdmin && System.currentTimeMillis() - admin.ts > adminPeriod =>
        admin = AdminStatus()
        logger.info(s"Admin mode has been disabled by time out ($adminPeriod milliseconds)")
        new SendMessage(chatId, badCommand) :: Nil

      // Bot moves saved file with price list to work directory
      case "/save" if admin.isAdmin =>
        if (PriceSource.move(new File(s"$priceDir/tmp/${admin.ts}"), new File(s"$priceDir/${admin.ts}"))) {
          logger.info(s"Price list has been moved successful")
          new SendMessage(chatId, successStatus) :: Nil
        }
        else
          new SendMessage(chatId, badCommand) :: Nil

      // Bot checks password and enables admin mode if password is true
      case password: String if admin.waitPass =>
        if (password == adminPassword) {
          admin = AdminStatus(isAdmin = true, chatId = chatId, ts = System.currentTimeMillis())
          logger.info(s"Password is true. User with userName:$userName has been enabled admin mode successful (chatId:$chatId)")
          new SendMessage(chatId, adminWelcome) :: Nil
        } else {
          admin = AdminStatus()
          logger.warn(s"Password is false. User with userName:$userName has not been enabled admin mode (chatId:$chatId)")
          new SendMessage(chatId, badCommand) :: Nil
        }

      // Bot saves taken messages with price list to temp directory as file.
      // Filename is timestamp when admin mode has been enabled
      case savePrice: String if admin.isAdmin =>
        val path = s"$priceDir/tmp/${admin.ts}"
        if (PriceSource.write(new File(path), savePrice)) {
          logger.info(s"User with userId:$userName has been wrote message in price list file $path (chatId:$chatId)")
          new SendMessage(chatId, successStatus) :: Nil
        }
        else new SendMessage(chatId, badCommand) :: Nil

      // It is reaction for otherwise messages
      case _ =>
        new SendMessage(chatId, badCommand) :: Nil
    }
  }

}
