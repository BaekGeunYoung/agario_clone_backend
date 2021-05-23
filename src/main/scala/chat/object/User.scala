package chat.`object`

import java.util.UUID

class User(
  val id: UUID,
  val username: String,
  val position: Position,
  val radius: Double
)
