package agario.domain.actor

import java.util.UUID
import agario.domain.model.{Position, Prey, Rooms, User}
import agario.domain.message.body.{EatBody, EatedBody, IncomingMessageBody, JoinBody, MergeBody, MergedBody, ObjectsBody, OutgoingMessageBody, PositionChangeBody, SeedBody, WasMergedBody}
import akka.actor._
import akka.event.Logging
import com.typesafe.config.ConfigFactory

import java.time.LocalDateTime
import scala.collection._
import scala.util.Random

object RoomActor {
  val configFactory = ConfigFactory.load()

  val roomHeight = configFactory.getDouble("roomHeight")
  val roomWidth = configFactory.getDouble("roomWidth")
  val initialRadius = configFactory.getDouble("initialRadius")
  val preyRadius = configFactory.getDouble("preyRadius")
  val preyMaxNumber = configFactory.getInt("preyMaxNumber")

  case class Join(userId: UUID, username: String)
  case class IncomingMessage(userId: UUID, body: IncomingMessageBody)
  case class OutgoingMessage(body: OutgoingMessageBody)
}

class RoomActor extends Actor {
  import RoomActor._
  import Rooms.rooms

  val log = Logging(context.system, this)

  var users: concurrent.Map[UUID, (User, ActorRef)] = concurrent.TrieMap.empty
  var preys: concurrent.Map[UUID, Prey] = initPreys

  private def initPreys: concurrent.Map[UUID, Prey] = supplyPreys(preyMaxNumber)

  private def supplyPreys(num: Int): concurrent.Map[UUID, Prey] =
    concurrent.TrieMap.from(
      (0 until num).map { _ =>
        val id = UUID.randomUUID()
        val prey = new Prey(id, genRandomPosition, preyRadius, genRandomColor)
        (id, prey)
      }
    )

  private def genRandomPosition: Position = {
    val x = Random.between(initialRadius, roomWidth - initialRadius)
    val y = Random.between(initialRadius, roomHeight - initialRadius)
    new Position(x, y)
  }

  private def genRandomColor: String = {
    val charSet = "0123456789abcdef"

    var color = "#"

    (0 until 6).foreach { _ =>
      color = color + charSet.charAt(Random.between(0, 16))
    }

    color
  }

  override def postRestart(t: Throwable) = println("=================restarted================")

  def receive = {
    case Join(userId, username) =>
      val newUser = new User(userId, username, genRandomPosition, initialRadius, genRandomColor)

      users += userId -> (newUser, sender())
      println(s"new user joined. current user list: ${users.map(_._2._1.id)}")

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
          println(s"${LocalDateTime.now()}: received POSITION_CHANGED")
          println(users.map(_._2._1.id))
          try {
            users(userId)._1.position = position
          } catch  {
            case e: Exception => println(e)
          }

          broadCast(ObjectsBody(users.map(_._2._1).toList, preys.values.toList))

        case MergeBody(colonyId) =>
          println(s"${LocalDateTime.now()}: received MERGE : colonyId: $colonyId, conquererId: $userId")
          // merge 가능한지 vaildation
          val conquererOption = users.get(userId)
          val colonyOption = users.get(colonyId)

          if (conquererOption.isDefined && colonyOption.isDefined) {
            val (conquerer, _) = conquererOption.get
            val (colony, colonyActor) = colonyOption.get
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
          }

        case EatBody(preyId) =>
          println(s"${LocalDateTime.now()}: received EAT, user: ${users(userId)._1.username}, preyId: ${preyId}")
          // eat 가능한지 validation
          val (eater, _) = users(userId)
          val prey = preys.get(preyId)

          if (prey.isDefined) {
            val distance = eater.position distanceFrom prey.get.position
            val canEat = distance <= eater.radius

            if (canEat) {
              eater.updateRadius(prey.get.radius)

              preys -= preyId

              // eated message를 모두에게 보냄
              println(s"${LocalDateTime.now()}: send EATED, eater: ${eater.username}, preyId: ${preyId}")
              broadCast(EatedBody(eater, preyId))

              // 먹이 갯수가 많이 떨어지면 seeding 해주기
              if (preys.size < preyMaxNumber / 2) {
                val newPreys = supplyPreys(preyMaxNumber / 2)

                preys ++= newPreys

                broadCast(SeedBody(newPreys.values.toList))
              }
            }
          }

      }
  }

  private def broadCast(messageBody: OutgoingMessageBody): Unit = users.values.foreach(_._2 ! OutgoingMessage(messageBody))
}
