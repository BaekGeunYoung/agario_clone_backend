package chat

import spray.json.JsValue

case class WSIncomingMessage(val `type`: IncomingMessageTypes.Value, val body: JsValue)
