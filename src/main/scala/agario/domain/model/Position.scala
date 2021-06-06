package agario.domain.model

class Position(var x: Double, val y: Double) {
  def distanceFrom(other: Position): Double =
    Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2))

  override def toString: String = s"x: $x, y: $y"
}
