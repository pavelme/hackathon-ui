package controllers

import play.api.mvc._

object ProductsController extends Controller {

  def products = Action {
    Ok(views.html.Products.products())
  }

}
