package ruby.entiny

final case class AdminStatus(isAdmin: Boolean = false, waitPass: Boolean = false, chatId: Long = 0L, ts: Long = 0L)