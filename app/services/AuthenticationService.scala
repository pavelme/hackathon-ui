package services

import scala.concurrent.Future

object AuthenticationService {

  def authenticate(login: String, password: String): Future[Option[String]] = {
    //to do: get token
    Future.successful(Some("sometoken"))
  }

  def register(login: String, password: String): Future[Option[String]] = {
    //to do: get token
    Future.successful(Some("sometoken"))
  }

}
