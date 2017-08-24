package pw.yurchenko.entiny

/**
  * The case class uses for changing user status in runtime
  *
  * @param isAdmin  Boolean (false by default)
  * @param waitPass Boolean (false by default)
  * @param chatId   Long (0 by default)
  * @param ts       Long (0 by default)
  * @author Anton Yurchenko
  */
final case class AdminStatus(isAdmin: Boolean = false, waitPass: Boolean = false, chatId: Long = 0L, ts: Long = 0L)