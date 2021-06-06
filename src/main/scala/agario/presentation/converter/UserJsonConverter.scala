package agario.presentation.converter

import java.util.UUID
import agario.domain.model.User
import spray.json.{DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat, enrichAny}

class UserJsonConverter() extends RootJsonFormat[User] {
  override def write(user: User): JsValue =
    JsObject(
      ("id", JsString(user.id.toString)),
      ("username", JsString(user.username)),
      ("position", PositionJsonConverter.toJsObject(user.position)),
      ("radius", JsNumber(user.radius)),
      ("color", JsString(user.color))
    )

  override def read(json: JsValue): User = {
    json match {
      case JsObject(fields) =>
        new User(
          UUID.fromString(fields("id").asInstanceOf[JsString].value),
          fields("username").asInstanceOf[JsString].value,
          PositionJsonConverter.fromJsObject(fields("position").asInstanceOf[JsObject]),
          fields("radius").asInstanceOf[JsNumber].value.toDouble,
          fields("color").asInstanceOf[JsString].value
        )
      case _ => throw DeserializationException("serialize failed")
    }
  }
}
