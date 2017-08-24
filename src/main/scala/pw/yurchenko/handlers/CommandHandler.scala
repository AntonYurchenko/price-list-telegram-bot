package pw.yurchenko.handlers

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message

/**
  * The trait contains general methods for handlers of commands for telegram bots
  *
  * @author Anton Yurchenko
  */
trait CommandHandler {

  /**
    * The method is handling message from chat with command
    *
    * @param msgWithCmd Message taken message
    * @return Seq[SendMessage]
    */
  def handle(msgWithCmd: Message): Seq[SendMessage]

}
