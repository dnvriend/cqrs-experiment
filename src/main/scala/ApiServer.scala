import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import registration.RegistrationSupervisor

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ApiServer extends App with ApiRoutes {

  implicit val system = ActorSystem("api-server")

  implicit val materializer = ActorMaterializer()

  implicit val ec = system.dispatcher

  val registrationSupervisor: ActorRef = system.actorOf(Props[RegistrationSupervisor], "registrationSupervisor")

  lazy val routes: Route = apiRoutes

  Http().bindAndHandle(apiRoutes, "localhost", 8080)

  println(s"Server online at http://localhost:8080/")

  Await.result(system.whenTerminated, Duration.Inf)
}