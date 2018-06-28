package registration

import akka.persistence.{PersistentActor, SaveSnapshotFailure, SaveSnapshotSuccess, SnapshotOffer}

class RegistrationActor extends PersistentActor {

  def persistenceId: String = s"registration-${state.id}"

  var state = Registration("0", "empty")

  def receiveCommand: Receive = {
    case SaveSnapshotSuccess(metadata) => // ...
    case SaveSnapshotFailure(metadata, reason) => // ...
    case s: String =>
      persist(s) { evt => state = state.update(evt) }
  }

  def receiveRecover: Receive = {
    case SnapshotOffer(_, s: Registration) =>
      println("offered state = " + s)
      state = s
    case evt: String =>
      state = state.update(evt)
  }
}
