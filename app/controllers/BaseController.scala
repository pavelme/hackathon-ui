package controllers

import java.util.Base64

import com.ning.http.client.AsyncHttpClient
import play.api.mvc.Controller

trait BaseController extends Controller {
  val client = new AsyncHttpClient()

  // http://localhost:20000/v1/syndication/wix-stores/fetch-product-info/aHR0cDovL3BhdmVsbWUud2l4LmNvbS9teXN0b3JlLXRlc3Rkcml2ZS0zIyFwcm9kdWN0LXBhZ2UvY3FoMS8zOTYwNzEzODMx

  val host = "http://localhost:20000/v1"
  def fetchProductInfo(url: String) = {
    client
      .prepareGet(host + "/syndication/wix-stores/fetch-product-info/" + Base64.getEncoder.encodeToString(url.getBytes("UTF-8")))
      .execute()
      .get()
      .getResponseBody("UTF-8")
  }
}
