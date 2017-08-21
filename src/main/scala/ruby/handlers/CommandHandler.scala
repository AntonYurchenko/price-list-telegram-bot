package ruby.handlers

import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Message

trait CommandHandler {

  def handle(messageWithCommand: Message):Seq[SendMessage]

}
