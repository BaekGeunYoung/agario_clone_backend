package agario.actor

import java.util.UUID

import akka.actor._
import agario.JsonProtocol._
import agario.`object`.{Position, User}
import agario.messagebody.{JoinBody, PositionChangeBody}
import agario.{OutgoingMessageTypes, Rooms, WSIncomingMessage, WSOutgoingMessage}
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.collection.mutable
import scala.util.Random

object RoomActor {
  val configFactory = ConfigFactory.load()

  val roomHeight = configFactory.getDouble("roomHeight")
  val roomWidth = configFactory.getDouble("roomWidth")
  val initialRadius = configFactory.getDouble("initalRadius")

  case class Join(userId: UUID, username: String)
  case class IncomingMessage(userId: UUID, message: WSIncomingMessage)
}

class RoomActor extends Actor {
  import RoomActor._
  import Rooms.rooms
  var users: mutable.Map[UUID, (User, ActorRef)] = mutable.Map.empty

  def receive = {
    case Join(userId, username) =>
      val x = Random.between(initialRadius, roomWidth - initialRadius)
      val y = Random.between(initialRadius, roomHeight - initialRadius)
      val pos = new Position(x, y)

      val newUser = new User(userId, username, pos, initialRadius)

      users += userId -> (newUser, sender())

      rooms = rooms.map { pair =>
        if (context.self == pair._1) (pair._1, users.size)
        else pair
      }

      broadCast(WSOutgoingMessage(OutgoingMessageTypes.join, JoinBody(newUser).toJson))

      context.watch(sender())

    case Terminated(user) =>
      val terminatedUser = users.filter { mapEntry => mapEntry._2._2 == user }.head
      users -= terminatedUser._1

    case IncomingMessage(userId, message) =>
      message.`type` match {
        case agario.IncomingMessageTypes.positionChanged =>
          val body = message.body.convertTo[PositionChangeBody]

          users(userId)._1.position = body.position

          // send OBJECTS

        case agario.IncomingMessageTypes.merge =>
        case agario.IncomingMessageTypes.eat =>
      }
  }

  def broadCast(msg: WSOutgoingMessage): Unit = users.values.foreach(_._2 ! msg)
}