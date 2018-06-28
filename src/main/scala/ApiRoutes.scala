import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import registration.RegistrationCommand.GetRegistration
import registration.{JsonSupport, Registration}

import scala.concurrent.Future
import scala.concurrent.duration._

trait ApiRoutes extends JsonSupport {

  implicit def system: ActorSystem

  def registrationSupervisor: ActorRef

  implicit lazy val timeout = Timeout(5.seconds) // usually we'd obtain the timeout from the system's configuration

  lazy val apiRoutes: Route =
    path("") {
      concat(
        get {
          val registration: Future[Registration] =
            (registrationSupervisor ? GetRegistration("1")).mapTo[Registration]
          complete(registration)
        },
        post {
          complete("Hello")
        })
    }

}
