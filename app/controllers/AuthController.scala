package controllers

import play.api.data._
import play.api.data.Forms._
import play.api.mvc.{Results, Action, Cookie, Controller}

import services.AuthenticationService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AuthController extends Controller {

  def login() = Action { implicit request =>
    Ok(views.html.Auth.login())
  }

  def register() = Action { implicit request =>
    Ok(views.html.Auth.register())
  }

  def loginForm = Form(
    tuple(
      "login" -> text,
      "password" -> text
    )
  )

  def signIn() = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      error =>
        Future.successful(Redirect(routes.AuthController.login()).flashing("error" -> "empty_fields")),
      dataTuple => {
        val (login, password) = dataTuple
        AuthenticationService.authenticate(login, password).map {
          case Some(token) =>
            Redirect("/").withCookies(Cookie("token", token, Some(999999999), "/"))
          case None =>
            Redirect(routes.AuthController.login()).flashing("error" -> "incorrect_credentials")
        }
      }
    )
  }

  def signUp() = Action.async { implicit request =>
    loginForm.bindFromRequest().fold(
      error =>
        Future.successful(Results.BadRequest),
      dataTuple => {
        val (login, password) = dataTuple
        AuthenticationService.register(login, password).map {
          case Some(token) =>
            Redirect("/").withCookies(Cookie("token", token, Some(999999999), "/"))
          case None =>
            Redirect(routes.AuthController.login()).flashing("error" -> "incorrect_credentials")
        }
      }
    )
  }

}
