package utils

import controllers.HttpClient
import models.User
import play.api.mvc._

import scala.concurrent.Future

class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

object Authenticated extends ActionBuilder[AuthenticatedRequest] with HttpClient{

  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
      request.cookies.get("token").map { tokenCookie =>
        val user = getLoggedUser(tokenCookie.value)
        block(new AuthenticatedRequest(user.get, request))
      }.getOrElse {
        Future.successful(Results.Redirect("/auth/login"))
      }
    }


}

