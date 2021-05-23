package chat

import chat.messagebody.JoinBody
import spray.json.DefaultJsonProtocol
import converter.{EnumJsonConverter, UserJsonConverter}

object JsonProtocol extends DefaultJsonProtocol {
  implicit val incomingMessageTypeConverter = new EnumJsonConverter(IncomingMessageTypes)
  implicit val wsIncomingMessageFormat = jsonFormat2(WSIncomingMessage)

  implicit val outgoingMessageTypeConverter = new EnumJsonConverter(OutgoingMessageTypes)
  implicit val wsOutgoingMessageFormat = jsonFormat2(WSOutgoingMessage)

  implicit val userConverter = new UserJsonConverter()
  implicit val joinBodyFormat = jsonFormat1(JoinBody)
}
