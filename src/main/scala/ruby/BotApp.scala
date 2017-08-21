package ruby

import org.telegram.telegrambots.{ApiContextInitializer, TelegramBotsApi}
import ruby.bots.PriceBot

object BotApp extends App {

  ApiContextInitializer.init()
  val botsApi = new TelegramBotsApi
  botsApi.registerBot(new PriceBot)

}
