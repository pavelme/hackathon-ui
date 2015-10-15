package services

import controllers.HttpClient

import scala.concurrent.Future

object AuthenticationService extends HttpClient {

  def authenticate(login: String, password: String): Future[Option[String]] = {
    Future.successful(signIn(login, password))
  }

  def register(login: String, password: String): Future[Option[String]] = {
    Future.successful(signUp(login, password))
  }

}
