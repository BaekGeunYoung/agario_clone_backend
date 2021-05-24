package agario.actor

import java.util.UUID

import akka.actor._
import agario.JsonProtocol._
import agario.`object`.{Position, Prey, User}
import agario.messagebody.{EatBody, JoinBody, MergeBody, MergedBody, ObjectsBody, PositionChangeBody}
import agario.{OutgoingMessageTypes, Rooms, WSIncomingMessage, WSOutgoingMessage}
import com.typesafe.config.ConfigFactory
import spray.json._

import scala.collection._
import scala.util.Random

object RoomActor {
  val configFactory = ConfigFactory.load()

  val roomHeight = configFactory.getDouble("roomHeight")
  val roomWidth = configFactory.getDouble("roomWidth")
  val initialRadius = configFactory.getDouble("initalRadius")
  val preyRadius = configFactory.getDouble("preyRadius")

  case class Join(userId: UUID, username: String)
  case class IncomingMessage(userId: UUID, message: WSIncomingMessage)
}

class RoomActor extends Actor {
  import RoomActor._
  import Rooms.rooms

  var users: concurrent.Map[UUID, (User, ActorRef)] = concurrent.TrieMap.empty
  var preys: concurrent.Map[UUID, Prey] = initPreys

  def initPreys: concurrent.Map[UUID, Prey] =
    concurrent.TrieMap.from(
      (0 until 100).map { _ =>
        val id = UUID.randomUUID()
        val prey = new Prey(id, genRandomPosition, preyRadius)
        (id, prey)
      }
    )

  private def genRandomPosition: Position = {
    val x = Random.between(initialRadius, roomWidth - initialRadius)
    val y = Random.between(initialRadius, roomHeight - initialRadius)
    new Position(x, y)
  }

  def receive = {
    case Join(userId, username) =>
      val newUser = new User(userId, username, genRandomPosition, initialRadius)

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

          broadCast(
            WSOutgoingMessage(
              OutgoingMessageTypes.objects,
              ObjectsBody(users.map(_._2._1).toList, preys.map(_._2).toList).toJson
            )
          )

        case agario.IncomingMessageTypes.merge =>
          val body = message.body.convertTo[MergeBody]
          // merge 가능한지 vaildation
          val (conquerer, _) = users(userId)
          val (colony, colonyActor) = users(body.colonyId)

          val distance = conquerer.position distanceFrom colony.position
          val canMerge = distance <= conquerer.radius

          if (canMerge) {
            conquerer.updateRadius(colony.radius)

            users -= body.colonyId

            // merged message를 모두에게 보냄
            broadCast(
              WSOutgoingMessage(
                OutgoingMessageTypes.merged,
                MergedBody(conquerer).toJson
              )
            )

            // merge 당한 유저에게는 wasMerged message를 보냄
            colonyActor ! WSOutgoingMessage(OutgoingMessageTypes.warsMerged, JsString(""))
          }
        case agario.IncomingMessageTypes.eat =>
          val body = message.body.convertTo[EatBody]
          // eat 가능한지 validation
          val (eater, _) = users(userId)
          val prey = preys(body.preyId)

          val distance = eater.position distanceFrom prey.position
          val canEat = distance <= eater.radius

          if (canEat) {
            eater.updateRadius(prey.radius)

            // eated message를 모두에게 보냄
            broadCast(
              WSOutgoingMessage(
                OutgoingMessageTypes.eated,
                MergedBody(eater).toJson
              )
            )
          }
      }
  }

  def broadCast(msg: WSOutgoingMessage): Unit = users.values.foreach(_._2 ! msg)
}
