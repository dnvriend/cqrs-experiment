package registration

import akka.actor.SupervisorStrategy
import akka.persistence.{PersistentActor, SaveSnapshotFailure, SaveSnapshotSuccess, SnapshotOffer}
import registration.RegistrationActor._

/**
  * Upon creation, this PersistentActor retrieves its state from the database
  * and is ready to receive messages.
  */
class RegistrationActor(id: String) extends PersistentActor {

  def persistenceId: String = s"registration-${state.id}"

  var state = Registration("0", "empty")

  def receiveCommand: Receive = {
    case msg@SaveSnapshotSuccess(metadata) =>
      println("Received: " + msg)
    case msg@SaveSnapshotFailure(metadata, reason) => // ...
      println("Received: " + msg)
    case msg@GetRegistration(id) =>
      println("Received: " + msg)
      sender() ! state
    case cmd: CreateRegistration =>
      println("Received: " + cmd)
      // store the actorRef of the sender
      val senderRef = sender()
      // Update state and persist the event in the journal
      persist(checkCommand(cmd)) { event =>
        // update state
        updateState(event)
        // Save snapshot on every state change. Normally you would do this every n changes.
        saveSnapshot(state)
        senderRef ! akka.actor.Status.Success(Registration(cmd.id, cmd.status))
      }
    case cmd@GetRegistration(id) =>

    case cmd: Cmd =>
      println("Received: " + cmd)
      // store the actorRef of the sender
      val senderRef = sender()
      // Update state and persist the event in the journal
      persist(checkCommand(cmd)) { event =>
        // update state
        updateState(event)
        // Save snapshot on every state change. Normally you would do this every n changes.
        saveSnapshot(state)
        senderRef ! akka.actor.Status.Success(Registration(cmd.id, "generic"))
      }
    case msg@SupervisorStrategy.Stop =>
      println("Received: " + msg)
      context.stop(self)
    case msg =>
      println("Unknown command received: " + msg)
  }

  def receiveRecover: Receive = {
    case SnapshotOffer(_, s: Registration) =>
      println("offered state = " + s)
      state = s
    case evt: Evt =>
      updateState(evt)
  }

  private def checkCommand: Cmd => Evt = {
    case CreateRegistration(id, status) =>
      RegistrationCreated(id, status)
    case UpdateRegistration(id, status) =>
      RegistrationUpdated(id, status)
    case DeleteRegistration(id) =>
      RegistrationDeleted(id)
  }

  private def updateState(evt: Evt): Unit = {
    state = state.updated(evt)
  }
}

object RegistrationActor {

  sealed trait Cmd { def id: String }

  case class CreateRegistration(id: String, status: String) extends Cmd

  case class UpdateRegistration(id: String, status: String) extends Cmd

  case class DeleteRegistration(id: String) extends Cmd

  case class GetRegistration(id: String) extends Cmd

  sealed trait Evt

  case class RegistrationCreated(id: String, status: String) extends Evt

  case class RegistrationUpdated(id: String, status: String) extends Evt

  case class RegistrationDeleted(id: String) extends Evt

}
