package agario.presentation.converter

import agario.domain.`object`.Position
import spray.json.{JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

object PositionJsonConverter {
  def toJsObject(pos: Position): JsObject = JsObject(("x", JsNumber(pos.x)), ("y", JsNumber(pos.y)))
  def fromJsObject(obj: JsObject): Position = new Position(
    obj.fields("x").asInstanceOf[JsNumber].value.toDouble,
    obj.fields("y").asInstanceOf[JsNumber].value.toDouble
  )
}

class PositionJsonConverter extends RootJsonFormat[Position] {
  override def write(obj: Position): JsValue = PositionJsonConverter.toJsObject(obj)

  override def read(json: JsValue): Position = PositionJsonConverter.fromJsObject(json.asInstanceOf[JsObject])
}
