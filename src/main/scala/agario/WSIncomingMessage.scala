package agario

import spray.json.JsValue

case class WSIncomingMessage(`type`: IncomingMessageTypes.Value, body: JsValue)
