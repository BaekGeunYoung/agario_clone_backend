package agario.`object`

import java.util.UUID

class User(
  val id: UUID,
  val username: String,
  var position: Position,
  var radius: Double
) {
  def updateRadius(smallRadius: Double): Double = {
    val areaSum = (Math.PI * radius * radius) + (Math.PI * smallRadius * smallRadius)
    Math.sqrt(areaSum / Math.PI)
  }
}
