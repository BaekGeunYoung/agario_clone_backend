package chat

object IncomingMessageTypes extends Enumeration {
  val positionChanged = Value("POSITION_CHANGED")
  val merge = Value("MERGE")
  val eat = Value("EAT")
}

object OutgoingMessageTypes extends Enumeration {
  val join = Value("JOIN")
  val objects = Value("OBJECTS")
  val merged = Value("MERGED")
  val warsMerged = Value("WAS_MERGED")
  val eated = Value("EATED")
}
