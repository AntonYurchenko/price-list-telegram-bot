package ruby.handlers

import java.io.File

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
class PriceBotCommandHandler extends CommandHandler {

  private var admin = AdminStatus()

  /**
    * The method is handling message from chat with command
    *
    * @param msgWithCmd Message taken message
    * @return Seq[SendMessage]
    */
  override def handle(msgWithCmd: Message): Seq[SendMessage] = msgWithCmd.getText match {

    // Bot sends start welcome message
    case "/start" =>
      val text = (startHeader :: commandsHeader :: commands).mkString("\n")
      new SendMessage(msgWithCmd.getChatId, text).enableHtml(true) :: Nil

    // Bot sends message with contacts
    case "/contact" =>
      val text = (contactsHeader :: contacts).mkString("\n")
      new SendMessage(msgWithCmd.getChatId, text).enableHtml(true) :: Nil

    // Bot sends message with information about available commands
    case "/help" =>
      val text = (commandsHeader :: commands).mkString("\n")
      new SendMessage(msgWithCmd.getChatId, text).enableHtml(true) :: Nil

    // Bot sends message with price list
    case "/price" =>
      val price = PriceSource.readActual(priceDir)
      if (price.isEmpty) new SendMessage(msgWithCmd.getChatId, priceNotAvailable) :: Nil
      else
        price.map(_.mkString("\n"))
          .map(text => new SendMessage(msgWithCmd.getChatId, text))

    //Bot enables password standby
    case "/admin" =>
      admin = AdminStatus(waitPass = true)
      new SendMessage(msgWithCmd.getChatId, badCommand).enableHtml(true) :: Nil

    // Bot disables admin mode by time out
    case _ if admin.isAdmin && System.currentTimeMillis() - admin.ts > adminPeriod =>
      admin = AdminStatus()
      new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil

    // Bot moves saved file with price list to work directory
    case "/save" if admin.isAdmin =>
      if (PriceSource.move(new File(s"$priceDir/tmp/${admin.ts}"), new File(s"$priceDir/${admin.ts}")))
        new SendMessage(msgWithCmd.getChatId, successStatus) :: Nil
      else
        new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil

    // Bot checks password and enables admin mode if password is true
    case password: String if admin.waitPass =>
      if (password == adminPassword) {
        admin = AdminStatus(isAdmin = true, chatId = msgWithCmd.getChatId, ts = System.currentTimeMillis())
        new SendMessage(msgWithCmd.getChatId, adminWelcome) :: Nil
      } else {
        admin = AdminStatus()
        new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil
      }

    // Bot saves taken messages with price list to temp directory as file.
    // Filename is timestamp when admin mode has been enabled
    case savePrice: String if admin.isAdmin =>
      if (PriceSource.write(new File(s"$priceDir/tmp/${admin.ts}"), savePrice))
        new SendMessage(msgWithCmd.getChatId, successStatus) :: Nil
      else new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil

    // It is reaction for otherwise messages
    case _ =>
      new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil
  }

}
