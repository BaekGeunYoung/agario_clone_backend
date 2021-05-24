package agario

import agario.messagebody.{EatBody, EatedBody, JoinBody, MergeBody, MergedBody, ObjectsBody, PositionChangeBody, SeedBody}
import spray.json.DefaultJsonProtocol
import converter.{EnumJsonConverter, PositionJsonConverter, PreyJsonConverter, UUIDJsonConverter, UserJsonConverter}

object JsonProtocol extends DefaultJsonProtocol {
  implicit val uuidConverter = new UUIDJsonConverter()

  implicit val incomingMessageTypeConverter = new EnumJsonConverter(IncomingMessageTypes)
  implicit val wsIncomingMessageConverter = jsonFormat2(WSIncomingMessage)

  implicit val outgoingMessageTypeConverter = new EnumJsonConverter(OutgoingMessageTypes)
  implicit val wsOutgoingMessageConverter = jsonFormat2(WSOutgoingMessage)

  // object converter
  implicit val positionConverter = new PositionJsonConverter()
  implicit val userConverter = new UserJsonConverter()
  implicit val preyConverter = new PreyJsonConverter()

  // ws message body converter
  implicit val joinBodyConverter = jsonFormat1(JoinBody)
  implicit val positionChangedBodyConverter = jsonFormat1(PositionChangeBody)
  implicit val objectsBodyConverter = jsonFormat2(ObjectsBody)
  implicit val mergeBodyConverter = jsonFormat1(MergeBody)
  implicit val mergedBodyConverter = jsonFormat1(MergedBody)
  implicit val eatBodyConverter = jsonFormat1(EatBody)
  implicit val eatedBodyConverter = jsonFormat1(EatedBody)
  implicit val seedBodyConverter = jsonFormat1(SeedBody)
}
