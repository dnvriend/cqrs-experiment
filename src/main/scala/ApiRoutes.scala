import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

trait ApiRoutes extends JsonSupport {

  implicit def system: ActorSystem

  lazy val apiRoutes: Route =
    path("") {
      concat(
      get {
        complete("Hello")
      },
      post {
        complete("Hello")
      })
    }

}
