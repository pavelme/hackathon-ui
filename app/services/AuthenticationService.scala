package services

import controllers.HttpClient

import scala.concurrent.Future

object AuthenticationService extends HttpClient {

  def authenticate(login: String, password: String): Future[Option[String]] = {
    //to do: get token
    Future.successful(Some("sometoken"))
  }

  def register(login: String, password: String): Future[Option[String]] = {
    //to do: get token
    Future.successful(Some("sometoken"))
  }

}
