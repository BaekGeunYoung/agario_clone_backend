package agario.domain.message.body

import agario.domain.model.{Prey, User}

import java.util.UUID

sealed trait OutgoingMessageBody

case class JoinBody(newUser: User) extends OutgoingMessageBody
case class ObjectsBody(users: List[User], preys: List[Prey]) extends OutgoingMessageBody
case class MergedBody(userAfterMerge: User, colonyId: UUID) extends OutgoingMessageBody
case object WasMergedBody extends OutgoingMessageBody
case class SeedBody(newPreys: List[Prey]) extends OutgoingMessageBody
case class EatedBody(userAfterEat: User, preyId: UUID) extends OutgoingMessageBody
