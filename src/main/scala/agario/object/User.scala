package agario.`object`

import java.util.UUID

class User(
  val id: UUID,
  val username: String,
  var position: Position,
  var radius: Double
)
