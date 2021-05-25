package agario

import agario.messagebody.{EatedBody, JoinBody, MergedBody, ObjectsBody, OutgoingMessageBody, SeedBody, WasMergedBody}
import spray.json.JsValue
import agario.JsonProtocol._
import spray.json._

case class WSOutgoingMessage(`type`: OutgoingMessageTypes.Value, body: JsValue)

object WSOutgoingMessageFactory {
  def fromMessageBody(messageBody: OutgoingMessageBody): WSOutgoingMessage = messageBody match {
    case body: JoinBody => WSOutgoingMessage(OutgoingMessageTypes.join, body.toJson)
    case body: ObjectsBody => WSOutgoingMessage(OutgoingMessageTypes.join, body.toJson)
    case body: MergedBody => WSOutgoingMessage(OutgoingMessageTypes.join, body.toJson)
    case body: SeedBody => WSOutgoingMessage(OutgoingMessageTypes.join, body.toJson)
    case body: EatedBody => WSOutgoingMessage(OutgoingMessageTypes.join, body.toJson)
    case WasMergedBody => WSOutgoingMessage(OutgoingMessageTypes.join, JsString(""))
  }
}
