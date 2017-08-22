package ruby.handlers

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ruby.sources.ConfigFile._
import ruby.sources.PriceSource

class PriceBotCommandHandler extends CommandHandler {

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

    case _ =>
      new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil
  }

}
