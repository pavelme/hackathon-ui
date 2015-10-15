package utils

import models.User
import play.api.mvc._

import scala.concurrent.Future

class AuthenticatedRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request)

object Authenticated extends ActionBuilder[AuthenticatedRequest] {

  def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]) = {
      request.cookies.get("token").map { tokenCookie =>
        // todo: get user by token
        block(new AuthenticatedRequest(User(0, "", "", 0, 0), request))
      }.getOrElse {
        Future.successful(Results.Redirect("/loginpage"))
      }
    }


}

