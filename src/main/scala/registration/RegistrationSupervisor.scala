package registration

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}
import registration.RegistrationCommand._

import scala.concurrent.duration._

/**
  * This Actor class receives messages from the REST endpoint.
  * If the message concerns a specific Registration, identified with `id`
  * then a new RegistrationActor is created and the message is forwarded
  * to this Actor.
  */
class RegistrationSupervisor() extends Actor with ActorLogging {

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 second) {
      case ResumeException => Resume
      case RestartException => Restart
      case StopException => Stop
      case _: Exception => Escalate
    }

  override def receive: Receive = {
    case GetRegistration(id) =>
      log.debug(s"Get registration ${id}")
      // TODO: get a registration actor and ask it for a registration
      val child = getRegistrationActor(id)
      child forward GetRegistration(id)
      //sender() ! Registration("1", "alive")
    case CreateRegistration(id, status) =>
      log.debug(s"Create registration ${id}")
      // TODO: get a registration actor ...
      sender() ! Registration("1", "alive")
    case UpdateRegistration(id, status) =>
      log.debug(s"Update registration ${id}")
      // TODO: get a registration actor ...
      sender() ! Registration("1", "alive")
  }

  private def getRegistrationActor(id: String) = {
    context.actorOf(Props(new RegistrationActor(id)), s"registration-${id}")
  }

}

object RegistrationCommand {

  case class GetRegistration(id: String)

  case class CreateRegistration(id: String, status: String)

  case class UpdateRegistration(id: String, status: String)

  case object ResumeException extends Exception

  case object StopException extends Exception

  case object RestartException extends Exception

}
