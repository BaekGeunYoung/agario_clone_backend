package agario.domain.actor

import java.util.UUID

import agario.domain.actor.RoomActor.{IncomingMessage, OutgoingMessage}
import akka.actor.{Actor, ActorRef}

object UserActor {
  case class Connected(outgoing: ActorRef, userId: UUID, userName: String)
}

class UserActor(room: ActorRef) extends Actor {
  import UserActor._

  def receive = {
    case Connected(outgoing, userId, username) =>
      context.become(connected(outgoing, userId, username))
  }

  def connected(outgoing: ActorRef, userId: UUID, username: String): Receive = {
    room ! RoomActor.Join(userId, username)

    {
      case incomingMessage: IncomingMessage =>
        room ! incomingMessage

      case outgoingMessage: OutgoingMessage =>
        outgoing ! outgoingMessage
    }
  }
}
