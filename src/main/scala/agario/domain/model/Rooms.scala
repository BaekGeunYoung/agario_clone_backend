package agario.domain.model

import agario.domain.actor.RoomActor
import akka.actor.{ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Rooms {
  private val roomCapacity: Int = ConfigFactory.load().getInt("roomCapacity")
  var rooms: List[(ActorRef, Int)] = List()

  def findOrCreate()(implicit actorSystem: ActorSystem): ActorRef = {
    if (rooms.isEmpty || rooms.last._2 >= roomCapacity) createNewRoom()
    else rooms.last._1
  }

  private def createNewRoom()(implicit actorSystem: ActorSystem): ActorRef = {
    val room = actorSystem.actorOf(Props(new RoomActor))
    rooms = rooms :+ (room, 0)

    room
  }
}
