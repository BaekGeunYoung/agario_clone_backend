package agario.domain.message.body

import java.util.UUID

import agario.domain.`object`.Position

sealed trait IncomingMessageBody

case class EatBody(preyId: UUID) extends IncomingMessageBody
case class MergeBody(colonyId: UUID) extends IncomingMessageBody
case class PositionChangeBody(position: Position) extends IncomingMessageBody
