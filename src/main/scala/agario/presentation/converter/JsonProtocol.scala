package agario.presentation.converter

import agario.domain.message.`type`.{IncomingMessageTypes, OutgoingMessageTypes}
import agario.domain.message.body.{EatBody, EatedBody, JoinBody, MergeBody, MergedBody, ObjectsBody, PositionChangeBody, SeedBody}
import agario.presentation.message.{WSIncomingMessage, WSOutgoingMessage}

object JsonProtocol extends SnakifiedSprayJsonSupport {
  implicit val uuidConverter = new UUIDJsonConverter()

  implicit val incomingMessageTypeConverter = new EnumJsonConverter(IncomingMessageTypes)
  implicit val wsIncomingMessageConverter = jsonFormat2(WSIncomingMessage)

  implicit val outgoingMessageTypeConverter = new EnumJsonConverter(OutgoingMessageTypes)
  implicit val wsOutgoingMessageConverter = jsonFormat2(WSOutgoingMessage)

  // object agario.presentation.converter
  implicit val positionConverter = new PositionJsonConverter()
  implicit val userConverter = new UserJsonConverter()
  implicit val preyConverter = new PreyJsonConverter()

  // ws message body agario.presentation.converter
  implicit val joinBodyConverter = jsonFormat1(JoinBody)
  implicit val positionChangedBodyConverter = jsonFormat1(PositionChangeBody)
  implicit val objectsBodyConverter = jsonFormat2(ObjectsBody)
  implicit val mergeBodyConverter = jsonFormat1(MergeBody)
  implicit val mergedBodyConverter = jsonFormat2(MergedBody)
  implicit val eatBodyConverter = jsonFormat1(EatBody)
  implicit val eatedBodyConverter = jsonFormat2(EatedBody)
  implicit val seedBodyConverter = jsonFormat1(SeedBody)
}
