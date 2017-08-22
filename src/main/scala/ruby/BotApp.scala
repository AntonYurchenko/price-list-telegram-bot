package ruby

import org.telegram.telegrambots.{ApiContextInitializer, TelegramBotsApi}
import ruby.bots.PriceBot

/**
  * The object is entry point of application
  *
  * @author Anton Yurchenko
  */
object BotApp extends App {

  ApiContextInitializer.init()
  val botsApi = new TelegramBotsApi
  botsApi.registerBot(new PriceBot)

}
