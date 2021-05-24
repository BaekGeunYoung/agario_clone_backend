package agario

import spray.json.JsValue

case class WSOutgoingMessage(`type`: OutgoingMessageTypes.Value, body: JsValue)
