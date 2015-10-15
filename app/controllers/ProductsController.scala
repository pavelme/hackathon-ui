package controllers

import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, Controller}

object ProductsController extends Controller {

  def products = Action {
    Ok(views.html.Products.products())
  }

  val productUrlForm = Form(
    "Product url" -> nonEmptyText
  )


  def getProducts =
    Action { implicit request =>
      productUrlForm.bindFromRequest.fold(
        errors => BadRequest(views.html.addProduct2(errors.errorsAsJson.toString)),
        url => {
          Ok(url)
        }
      )
    }


  def sendProductUrl = Action {
    Ok(views.html.addProduct(productUrlForm))
  }

}
