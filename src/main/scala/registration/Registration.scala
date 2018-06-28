package registration

case class Registration(id: String, status: String) {
  def update(status: String): Registration = {
    copy(status = status)
  }
}
