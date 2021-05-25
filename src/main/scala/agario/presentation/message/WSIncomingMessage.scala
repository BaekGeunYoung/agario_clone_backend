package agario.presentation.message

import agario.domain.message.`type`.IncomingMessageTypes
import agario.domain.message.body.{EatBody, IncomingMessageBody, MergeBody, PositionChangeBody}
import spray.json._
import agario.presentation.converter.JsonProtocol._

case class WSIncomingMessage(`type`: IncomingMessageTypes.Value, body: JsValue) {
  def toMessageBody: IncomingMessageBody = `type` match {
    case IncomingMessageTypes.positionChanged => body.convertTo[PositionChangeBody]
    case IncomingMessageTypes.merge => body.convertTo[MergeBody]
    case IncomingMessageTypes.eat => body.convertTo[EatBody]
  }
}
