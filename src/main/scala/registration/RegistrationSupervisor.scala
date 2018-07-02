package registration

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, ActorLogging, OneForOneStrategy, Props}

import scala.concurrent.duration._

/**
  * This Actor class receives messages from the REST endpoint.
  * If the message concerns a specific Registration, identified with `id`
  * then a new RegistrationActor is created and the message is forwarded
  * to this Actor.
  */
class RegistrationSupervisor() extends Actor with ActorLogging {

  import RegistrationActor._

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1.second) {
      //      case ResumeException => Resume
      //      case RestartException => Restart
      //      case StopException => Stop
      case _: Exception => Escalate
    }

  override def receive: Receive = {
    case GetRegistration(id) =>
      log.debug(s"Get registration ${id}")
      val child = getRegistrationActor(id)
      child forward GetRegistration(id)
    case CreateRegistration(id, status) =>
      log.debug(s"Create registration ${id}")
      val child = getRegistrationActor(id)
      child forward CreateRegistration(id, status)
    case UpdateRegistration(id, status) =>
      log.debug(s"Update registration ${id}")
      // TODO: get a registration actor ...
      sender() ! Registration("1", "alive")
  }

  private def getRegistrationActor(id: String) = {
    context.actorOf(Props(new RegistrationActor(id)), s"registration-${id}")
  }

}
