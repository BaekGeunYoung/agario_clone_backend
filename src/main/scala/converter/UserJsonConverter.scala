package converter

import java.util.UUID

import chat.`object`.User
import spray.json.{DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

class UserJsonConverter() extends RootJsonFormat[User] {
  override def write(user: User): JsValue =
    JsObject(
      ("id", JsString(user.id.toString)),
      ("username", JsString(user.username)),
      ("position", PositionConverter.toJsObject(user.position)),
      ("radius", JsNumber(user.radius))
    )

  override def read(json: JsValue): User = {
    json match {
      case JsObject(fields) =>
        new User(
          UUID.fromString(fields["id"].asInstanceOf[JsString].value),
          fields["username"].asInstanceOf[JsString].value,
          PositionConverter.fromJsObject(fields["position"].asInstanceOf[JsObject]),
          fields["radius"].asInstanceOf[JsNumber].value.toDouble
        )
      case _ => throw DeserializationException("serialize failed")
    }
  }
}
