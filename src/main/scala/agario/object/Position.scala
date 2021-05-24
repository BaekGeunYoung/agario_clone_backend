package agario.`object`

class Position(val x: Double, val y: Double) {
  def distanceFrom(other: Position): Double =
    Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2))
}
