package converter

import java.util.UUID

import spray.json.{DeserializationException, JsString, JsValue, RootJsonFormat}

class UUIDJsonConverter extends RootJsonFormat[UUID] {
  override def write(uuid: UUID): JsValue =
    JsString(uuid.toString)

  override def read(json: JsValue): UUID = {
    json match {
      case JsString(str) => UUID.fromString(str)
      case _ => throw DeserializationException("serialize failed")
    }
  }
}
