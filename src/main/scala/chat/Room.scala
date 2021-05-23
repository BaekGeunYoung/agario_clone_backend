package chat

import java.util.UUID

import akka.actor._
import chat.`object`.{Position, User}
import chat.messagebody.JoinBody
import com.typesafe.config.ConfigFactory
import chat.JsonProtocol._
import spray.json._

import scala.util.Random

object Room {
  val configFactory = ConfigFactory.load()

  val roomHeight = configFactory.getDouble("roomHeight")
  val roomWidth = configFactory.getDouble("roomWidth")
  val initialRadius = configFactory.getDouble("initalRadius")

  case class Join(userId: UUID, username: String)
  case class IncomingMessage(userId: UUID, message: WSIncomingMessage)
}

class Room extends Actor {
  import Room._
  import Rooms.rooms
  var users: Map[User, ActorRef] = Map.empty

  def receive = {
    case Join(userId, username) =>
      val x = Random.between(initialRadius, roomWidth - initialRadius)
      val y = Random.between(initialRadius, roomHeight - initialRadius)
      val pos = new Position(x, y)

      val newUser = new User(userId, username, pos, initialRadius)

      users += newUser -> sender()

      rooms = rooms.map { pair =>
        if (context.self == pair._1) (pair._1, users.size)
        else pair
      }

      broadCast(WSOutgoingMessage(OutgoingMessageTypes.join, JoinBody(newUser).toJson))

      context.watch(sender())

    case Terminated(user) =>
      val terminatedUser = users.filter { mapEntry => mapEntry._2 == user }.head
      users -= terminatedUser._1

    case IncomingMessage(userId, message) =>
      message.`type` match {
        case chat.IncomingMessageTypes.positionChanged =>
        case chat.IncomingMessageTypes.merge =>
        case chat.IncomingMessageTypes.eat =>
      }
  }

  def broadCast(msg: WSOutgoingMessage): Unit = users.values.foreach(_ ! msg)
}
