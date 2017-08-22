package ruby.handlers

import java.io.File

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ruby.entiny.AdminStatus
import ruby.sources.ConfigFile._
import ruby.sources.PriceSource

class PriceBotCommandHandler extends CommandHandler {

  private var admin = AdminStatus()

  override def handle(msgWithCmd: Message): Seq[SendMessage] = msgWithCmd.getText match {

    case "/start" =>
      val text = (startHeader :: commandsHeader :: commands).mkString("\n")
      new SendMessage(msgWithCmd.getChatId, text).enableHtml(true) :: Nil

    case "/contact" =>
      val text = (contactsHeader :: contacts).mkString("\n")
      new SendMessage(msgWithCmd.getChatId, text).enableHtml(true) :: Nil

    case "/help" =>
      val text = (commandsHeader :: commands).mkString("\n")
      new SendMessage(msgWithCmd.getChatId, text).enableHtml(true) :: Nil

    case "/price" =>
      val price = PriceSource.readActual(priceDir)
      if (price.isEmpty) new SendMessage(msgWithCmd.getChatId, priceNotAvailable) :: Nil
      else
        price.map(_.mkString("\n"))
          .map(text => new SendMessage(msgWithCmd.getChatId, text))

    case "/admin" =>
      admin = AdminStatus(waitPass = true)
      new SendMessage(msgWithCmd.getChatId, badCommand).enableHtml(true) :: Nil

    case _ if admin.isAdmin && System.currentTimeMillis() - admin.ts > adminPeriod =>
      admin = AdminStatus()
      new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil

    case "/save" if admin.isAdmin =>
      if (PriceSource.move(new File(s"$priceDir/tmp/${admin.ts}"), new File(s"$priceDir/${admin.ts}")))
        new SendMessage(msgWithCmd.getChatId, successStatus) :: Nil
      else
        new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil

    case password: String if admin.waitPass =>
      if (password == adminPassword) {
        admin = AdminStatus(isAdmin = true, chatId = msgWithCmd.getChatId, ts = System.currentTimeMillis())
        new SendMessage(msgWithCmd.getChatId, adminWelcome) :: Nil
      } else {
        admin = AdminStatus()
        new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil
      }

    case savePrice: String if admin.isAdmin =>
      if (PriceSource.write(new File(s"$priceDir/tmp/${admin.ts}"), savePrice))
        new SendMessage(msgWithCmd.getChatId, successStatus) :: Nil
      else new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil

    case _ =>
      new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil
  }

}
