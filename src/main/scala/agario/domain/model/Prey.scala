package agario.domain.model

import java.util.UUID

class Prey(
    val id: UUID,
    val position: Position,
    val radius: Double,
    val color: String
)
