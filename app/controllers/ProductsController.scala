package controllers

import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages.Implicits._
import play.api.mvc.{Action, Controller}

object ProductsController extends BaseController {

  def products = Action {
//    Ok(views.html.Products.products())

    val response = client.prepareGet("http://sonatype.com").execute().get()


//    Ok(views.html.Products.products())
    Ok(response.getResponseBody)
  }

  def productsTest = Action {
    //    Ok(views.html.Products.products())

    val productInfo = fetchProductInfo("http://pavelme.wix.com/mystore-testdrive-3#!product-page/cqh1/3960713831")


    //    Ok(views.html.Products.products())
    Ok(productInfo)
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
