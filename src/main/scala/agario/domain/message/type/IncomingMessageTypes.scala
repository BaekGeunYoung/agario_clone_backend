package agario.domain.message.`type`

object IncomingMessageTypes extends Enumeration {
  val positionChanged = Value("POSITION_CHANGED")
  val merge = Value("MERGE")
  val eat = Value("EAT")
}

