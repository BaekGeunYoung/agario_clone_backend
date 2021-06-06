package agario.domain.model

import java.util.UUID

class User(
  val id: UUID,
  val username: String,
  var position: Position,
  var radius: Double,
  val color: String
) {
  def updateRadius(smallRadius: Double): Unit = {
    val areaSum = (Math.PI * radius * radius) + (Math.PI * smallRadius * smallRadius)
    this.radius = Math.sqrt(areaSum / Math.PI)
  }
}
