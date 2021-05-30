package agario.presentation.message

import agario.domain.message.`type`.OutgoingMessageTypes
import agario.domain.message.body._
import agario.presentation.converter.JsonProtocol._
import spray.json.{JsValue, _}

case class WSOutgoingMessage(`type`: OutgoingMessageTypes.Value, body: JsValue)

object WSOutgoingMessageFactory {
  def fromMessageBody(messageBody: OutgoingMessageBody): WSOutgoingMessage = messageBody match {
    case body: JoinBody => WSOutgoingMessage(OutgoingMessageTypes.join, body.toJson)
    case body: ObjectsBody => WSOutgoingMessage(OutgoingMessageTypes.objects, body.toJson)
    case body: MergedBody => WSOutgoingMessage(OutgoingMessageTypes.merged, body.toJson)
    case body: SeedBody => WSOutgoingMessage(OutgoingMessageTypes.seed, body.toJson)
    case body: EatedBody => WSOutgoingMessage(OutgoingMessageTypes.eated, body.toJson)
    case WasMergedBody => WSOutgoingMessage(OutgoingMessageTypes.wasMerged, JsString(""))
  }
}
