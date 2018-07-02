package registration

import registration.RegistrationActor.{Evt, RegistrationCreated, RegistrationDeleted, RegistrationUpdated}

case class Registration(id: String, status: String) {
  def updated(evt: Evt): Registration = evt match {
    case RegistrationCreated(id: String, status: String) =>
      Registration(id = id, status = status)
    case RegistrationUpdated(id: String, status: String) =>
      Registration(id = id, status = status)
    case RegistrationDeleted(id: String) =>
      Registration(id = id, status = "deleted")
  }
}
