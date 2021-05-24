package converter

import java.util.UUID

import agario.`object`.{Prey, User}
import spray.json.{DeserializationException, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

class PreyJsonConverter extends RootJsonFormat[Prey] {
  override def write(prey: Prey): JsValue =
    JsObject(
      ("id", JsString(prey.id.toString)),
      ("position", PositionJsonConverter.toJsObject(prey.position)),
      ("radius", JsNumber(prey.radius))
    )

  override def read(json: JsValue): Prey = {
    json match {
      case JsObject(fields) =>
        new Prey(
          UUID.fromString(fields["id"].asInstanceOf[JsString].value),
          PositionJsonConverter.fromJsObject(fields["position"].asInstanceOf[JsObject]),
          fields["radius"].asInstanceOf[JsNumber].value.toDouble
        )
      case _ => throw DeserializationException("serialize failed")
    }
  }
}
