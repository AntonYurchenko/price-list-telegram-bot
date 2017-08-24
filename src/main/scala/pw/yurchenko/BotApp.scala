package pw.yurchenko

import com.typesafe.scalalogging.LazyLogging
import org.telegram.telegrambots.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.{ApiContextInitializer, TelegramBotsApi}
import pw.yurchenko.bots.PriceBot
import pw.yurchenko.sources.ConfigFile._

/**
  * The object is entry point of application
  *
  * @author Anton Yurchenko
  */
object BotApp extends App with LazyLogging {

  ApiContextInitializer.init()
  val botsApi = new TelegramBotsApi

  try botsApi.registerBot(new PriceBot)
  catch {
    case ex: TelegramApiRequestException =>
      logger.error(s"Registration of $botUserName has been failed", ex)
      System.exit(1)
  }

  logger.info(s"Registration of $botUserName has been success")

}
