package agario.domain.message.body

import agario.domain.model.{Prey, User}

sealed trait OutgoingMessageBody

case class JoinBody(newUser: User) extends OutgoingMessageBody
case class ObjectsBody(users: List[User], preys: List[Prey]) extends OutgoingMessageBody
case class MergedBody(userAfterMerge: User) extends OutgoingMessageBody
case object WasMergedBody extends OutgoingMessageBody
case class SeedBody(newPreys: List[Prey]) extends OutgoingMessageBody
case class EatedBody(userAfterEat: User) extends OutgoingMessageBody
