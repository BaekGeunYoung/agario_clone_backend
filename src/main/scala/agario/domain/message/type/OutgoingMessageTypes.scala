package agario.domain.message.`type`

object OutgoingMessageTypes extends Enumeration {
  val join = Value("JOIN")
  val objects = Value("OBJECTS")
  val merged = Value("MERGED")
  val wasMerged = Value("WAS_MERGED")
  val eated = Value("EATED")
  val seed = Value("SEED")
}
