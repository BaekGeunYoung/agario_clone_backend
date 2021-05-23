package converter

import chat.`object`.Position
import spray.json.{JsNumber, JsObject}

object PositionConverter {
  def toJsObject(pos: Position): JsObject = JsObject(("x", JsNumber(pos.x)), ("y", JsNumber(pos.y)))
  def fromJsObject(obj: JsObject): Position = new Position(
    obj.fields["x"].asInstanceOf[JsNumber].value.toDouble,
    obj.fields["y"].asInstanceOf[JsNumber].value.toDouble
  )
}
