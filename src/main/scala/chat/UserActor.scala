package chat

import java.util.UUID

import akka.actor.{Actor, ActorRef}

object UserActor {
  case class Connected(outgoing: ActorRef, userId: UUID, userName: String)
  case class IncomingMessage(text: String, author: UUID)
  case class OutgoingMessage(text: String, author: UUID)
}

class UserActor(room: ActorRef) extends Actor {
  import UserActor._

  def receive = {
    case Connected(outgoing, userId, username) =>
      context.become(connected(outgoing, userId, username))
  }

  def connected(outgoing: ActorRef, userId: UUID, username: String): Receive = {
    room ! Room.Join(userId, username)

    {
      case incomingMessage: WSIncomingMessage =>
        room ! incomingMessage

      case outgoingMessage: WSOutgoingMessage =>
        outgoing ! outgoingMessage
    }
  }
}
