package agario.presentation

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.io.StdIn

object Server {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  def main(args: Array[String]): Unit = {
    val binding = Http().newServerAt("127.0.0.1", 8080).bind(Router.route)

    println("Started server at 127.0.0.1:8080, press enter to kill server")
    StdIn.readLine()

    binding
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
