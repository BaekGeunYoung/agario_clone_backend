package agario.messagebody

import java.util.UUID

import agario.`object`.Position

sealed trait IncomingMessageBody

case class EatBody(preyId: UUID) extends IncomingMessageBody
case class MergeBody(colonyId: UUID) extends IncomingMessageBody
case class PositionChangeBody(position: Position) extends IncomingMessageBody
