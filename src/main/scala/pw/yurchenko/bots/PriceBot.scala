package pw.yurchenko.bots

import org.telegram.telegrambots.api.objects.{Message, Update}
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.api.methods._
import pw.yurchenko.handlers.PriceBotCommandHandler
import pw.yurchenko.sources.ConfigFile._

/**
  * The class is implement of bot for sending price list.
  *
  * @author Anton Yurchenko
  */
class PriceBot extends TelegramLongPollingBot {

  private val cmdHandler = new PriceBotCommandHandler

  /**
    * The method return token of bot
    *
    * @return String
    */
  override def getBotToken: String = botToken

  /**
    * The method return name of bot
    *
    * @return String
    */
  override def getBotUsername: String = botUserName

  /**
    * This method will be called when an Update is received by the bot
    *
    * @param update Update it is [[https://core.telegram.org/bots/api#update update]] entity.
    */
  override def onUpdateReceived(update: Update): Unit = {
    cmdHandler.handle(update.getMessage).foreach(execute[Message, BotApiMethod[Message]])
  }

}
