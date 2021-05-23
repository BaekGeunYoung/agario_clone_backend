package agario

import spray.json.JsValue

case class WSOutgoingMessage(val `type`: OutgoingMessageTypes.Value, val body: JsValue)
