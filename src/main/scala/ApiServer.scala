import akka.actor.ActorSystem

import scala.concurrent.{Await, Future}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.duration.Duration

object ApiServer extends App with ApiRoutes {

  implicit val system = ActorSystem("my-system")

  implicit val materializer = ActorMaterializer()

  implicit val ec = system.dispatcher

  lazy val routes: Route = apiRoutes

  Http().bindAndHandle(apiRoutes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

  Await.result(system.whenTerminated, Duration.Inf)
}