package ruby.bots

import org.telegram.telegrambots.api.objects.{Message, Update}
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.api.methods._
import ruby.handlers.PriceBotCommandHandler


class PriceBot extends TelegramLongPollingBot {

  private val cmdHandler = new PriceBotCommandHandler

  /**
    * The method return token of bot
    *
    * @return String
    */
  override def getBotToken: String =
    "441623574:AAHTUxc8bBJzpb6LSzkLiMI3pHTvRwVJpjk"

  /**
    * The method return name of bot
    *
    * @return String
    */
  override def getBotUsername: String =
    "RubyPriceBot"

  /**
    * This method will be called when an Update is received by the bot
    *
    * @param update Update it is [[https://core.telegram.org/bots/api#update update]] entity.
    */
  override def onUpdateReceived(update: Update): Unit = {
      cmdHandler.handle(update.getMessage).foreach(execute[Message, BotApiMethod[Message]])
  }

}
