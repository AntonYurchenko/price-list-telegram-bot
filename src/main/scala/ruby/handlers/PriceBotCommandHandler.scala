package ruby.handlers

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message
import ruby.sources.ConfigFile._

import scala.io.Source

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
      val is = this.getClass.getResourceAsStream("/price/21082017.txt")
      val price = Source.fromInputStream(is).getLines().toSeq

      if (price.length <= 100) new SendMessage(msgWithCmd.getChatId, price.mkString("\n")) :: Nil
      else price.grouped(100).map(_.mkString("\n")).map(new SendMessage(msgWithCmd.getChatId, _)).toSeq

    case _ =>
      new SendMessage(msgWithCmd.getChatId, badCommand) :: Nil
  }

}
