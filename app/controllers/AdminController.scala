package controllers

import play.api.mvc.Controller
import utils.Authenticated

import scala.concurrent.Future

object AdminController extends Controller {

  def products =
    Authenticated.async { implicit req =>
      Future.successful(Ok(views.html.Admin.products()))
    }

  def promoting =
    Authenticated.async { implicit req =>
      Future.successful(Ok(views.html.Admin.promoting()))
    }

}
