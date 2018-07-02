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
    case SaveSnapshotSuccess(metadata) => // ...
    case SaveSnapshotFailure(metadata, reason) => // ...
    case GetRegistration(id) =>
      sender() ! state
    case cmd: Cmd =>
      // Update state and persist the event in the journal
      persist(checkCommand(cmd))(updateState)
      // Save snapshot on every state change. Normally you would do this every n changes.
      saveSnapshot(state)
    case SupervisorStrategy.Stop =>
      context.stop(self)
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

  sealed trait Cmd

  case class CreateRegistration(id: String, status: String) extends Cmd

  case class UpdateRegistration(id: String, status: String) extends Cmd

  case class DeleteRegistration(id: String) extends Cmd

  case class GetRegistration(id: String) extends Cmd

  sealed trait Evt

  case class RegistrationCreated(id: String, status: String) extends Evt

  case class RegistrationUpdated(id: String, status: String) extends Evt

  case class RegistrationDeleted(id: String) extends Evt

}
