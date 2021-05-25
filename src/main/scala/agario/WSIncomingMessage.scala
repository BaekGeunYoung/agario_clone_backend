package agario

import agario.messagebody.{EatBody, IncomingMessageBody, MergeBody, PositionChangeBody}
import spray.json._
import agario.JsonProtocol._

case class WSIncomingMessage(`type`: IncomingMessageTypes.Value, body: JsValue) {
  def toMessageBody: IncomingMessageBody = `type` match {
    case agario.IncomingMessageTypes.positionChanged => body.convertTo[PositionChangeBody]
    case agario.IncomingMessageTypes.merge => body.convertTo[MergeBody]
    case agario.IncomingMessageTypes.eat => body.convertTo[EatBody]
  }
}
