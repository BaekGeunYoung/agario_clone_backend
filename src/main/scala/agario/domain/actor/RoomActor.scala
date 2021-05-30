package agario.domain.actor

import java.util.UUID

import agario.domain.model.{Position, Prey, Rooms, User}
import agario.domain.message.body.{EatBody, IncomingMessageBody, JoinBody, MergeBody, MergedBody, ObjectsBody, OutgoingMessageBody, PositionChangeBody, SeedBody, WasMergedBody}
import akka.actor._
import com.typesafe.config.ConfigFactory

import scala.collection._
import scala.util.Random

object RoomActor {
  val configFactory = ConfigFactory.load()

  val roomHeight = configFactory.getDouble("roomHeight")
  val roomWidth = configFactory.getDouble("roomWidth")
  val initialRadius = configFactory.getDouble("initialRadius")
  val preyRadius = configFactory.getDouble("preyRadius")

  case class Join(userId: UUID, username: String)
  case class IncomingMessage(userId: UUID, body: IncomingMessageBody)
  case class OutgoingMessage(body: OutgoingMessageBody)
}

class RoomActor extends Actor {
  import RoomActor._
  import Rooms.rooms

  var users: concurrent.Map[UUID, (User, ActorRef)] = concurrent.TrieMap.empty
  var preys: concurrent.Map[UUID, Prey] = initPreys

  private def initPreys: concurrent.Map[UUID, Prey] = supplyPreys(100)

  private def supplyPreys(num: Int): concurrent.Map[UUID, Prey] =
    concurrent.TrieMap.from(
      (0 until num).map { _ =>
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

      broadCast(JoinBody(newUser))

      context.watch(sender())

    case Terminated(user) =>
      val terminatedUser = users.filter { mapEntry => mapEntry._2._2 == user }.head
      users -= terminatedUser._1

    case IncomingMessage(userId, message) =>
      message match {
        case PositionChangeBody(position) =>
          users(userId)._1.position = position

          broadCast(ObjectsBody(users.map(_._2._1).toList, preys.values.toList))

        case MergeBody(colonyId) =>
          // merge 가능한지 vaildation
          val (conquerer, _) = users(userId)
          val (colony, colonyActor) = users(colonyId)

          val distance = conquerer.position distanceFrom colony.position
          val canMerge = distance <= conquerer.radius

          if (canMerge) {
            conquerer.updateRadius(colony.radius)

            users -= colonyId

            // merged message를 모두에게 보냄
            broadCast(MergedBody(conquerer, colonyId))

            // merge 당한 유저에게는 wasMerged message를 보냄
            colonyActor ! OutgoingMessage(WasMergedBody)
          }

        case EatBody(preyId) =>
          // eat 가능한지 validation
          val (eater, _) = users(userId)
          val prey = preys(preyId)

          val distance = eater.position distanceFrom prey.position
          val canEat = distance <= eater.radius

          if (canEat) {
            eater.updateRadius(prey.radius)

            preys -= preyId

            // eated message를 모두에게 보냄
            broadCast(MergedBody(eater, preyId))

            // 먹이 갯수가 많이 떨어지면 seeding 해주기
            if (preys.size < 50) {
              val newPreys = supplyPreys(50)

              preys ++= newPreys

              broadCast(SeedBody(newPreys.values.toList))
            }
          }
      }
  }

  private def broadCast(messageBody: OutgoingMessageBody): Unit = users.values.foreach(_._2 ! OutgoingMessage(messageBody))
}
