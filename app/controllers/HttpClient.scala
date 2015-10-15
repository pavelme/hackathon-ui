package controllers

import java.util.Base64

import com.ning.http.client.AsyncHttpClient
import models.{ProductEntity, User, TokenEntity}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Reads, JsPath, Writes}

trait HttpClient extends JsonHelper {
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

  def signIn(login: String, password: String): Option[String] = {
    val response = client
      .preparePost(host + "/auth/signIn")
      .setBody(s"""{"login":"${login}","password":"${password}"}""")
      .addHeader("Content-Type", "application/json")
      .execute()
      .get()
      .getResponseBody("UTF-8")

    Json.parse(response).validate[TokenEntity].map(_.token).asOpt
  }

  def signUp(login: String, password: String): Option[String] = {
    val response = client
      .preparePost(host + "/auth/signUp")
      .setBody(s"""{"username":"${login}","password":"${password}"}""")
      .addHeader("Content-Type", "application/json")
      .execute()
      .get()
      .getResponseBody("UTF-8")

    Json.parse(response).validate[TokenEntity].map(_.token).asOpt
  }

  def getLoggedUser(token: String): Option[User] = {
    val response = client
      .prepareGet(host + "/users/me")
      .addHeader("Content-Type", "application/json")
      .addHeader("token", token)
      .execute()
      .get()
      .getResponseBody("UTF-8")

    Json.parse(response).validate[UserResponse].map { user =>
      User(
      user.id,
      user.username,
      user.password,
      user.balance,
      user.crossSaleBalance,
      token
      )
    }.asOpt
  }

  def getProducts(token: String):Option[List[ProductEntity]] = {
    val response = client
      .prepareGet(host + "/products")
      .addHeader("Content-Type", "application/json")
      .addHeader("token", token)
      .execute()
      .get()
      .getResponseBody("UTF-8")

    println("Json.parse(response).validate[List[ProductEntity]] = " + Json.parse(response).validate[List[ProductEntity]])
    println("Json.parse(response) = " + Json.parse(response))
    Json.parse(response).validate[List[ProductEntity]].asOpt
  }
}

trait JsonHelper {
  implicit val tokenWriter: Writes[TokenEntity] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "userId").write[Long] and
      (JsPath \ "token").write[String]
    )(unlift(TokenEntity.unapply))

  implicit val tokenReader: Reads[TokenEntity] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "userId").read[Long] and
      (JsPath \ "token").read[String]
    )(TokenEntity.apply _)

  implicit val userWriter: Writes[UserResponse] = (
      (JsPath \ "id").write[Long] and
      (JsPath \ "username").write[String] and
      (JsPath \ "password").write[String] and
      (JsPath \ "balance").write[Long] and
      (JsPath \ "crossSaleBalance").write[Long]
    )(unlift(UserResponse.unapply))
  
  implicit val userReader: Reads[UserResponse] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "username").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "balance").read[Long] and
      (JsPath \ "crossSaleBalance").read[Long]
    )(UserResponse.apply _)

  implicit val productWriter: Writes[ProductEntity] = (
    (JsPath \ "id").write[Long] and
      (JsPath \ "authorId").write[Long] and
      (JsPath \ "category").write[String] and
      (JsPath \ "title").write[String] and
      (JsPath \ "productUrl").write[String] and
      (JsPath \ "clickPrice").write[Double] and
      (JsPath \ "campaignBudget").write[Double] and
      (JsPath \ "viewsCount").write[Long] and
      (JsPath \ "clicksCount").write[Long] and
      (JsPath \ "productImage").write[String] and
      (JsPath \ "productPrice").write[Double] and
      (JsPath \ "isCrossSellEnabled").write[Boolean] and
      (JsPath \ "isBannerSellingEnabled").write[Boolean] and
      (JsPath \ "country").write[String]
    )(unlift(ProductEntity.unapply))

  implicit val productReader: Reads[ProductEntity] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "authorId").read[Long] and
      (JsPath \ "category").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "productUrl").read[String] and
      (JsPath \ "clickPrice").read[Double] and
      (JsPath \ "campaignBudget").read[Double] and
      (JsPath \ "viewsCount").read[Long] and
      (JsPath \ "clicksCount").read[Long] and
      (JsPath \ "productImage").read[String] and
      (JsPath \ "productPrice").read[Double] and
      (JsPath \ "isCrossSellEnabled").read[Boolean] and
      (JsPath \ "isBannerSellingEnabled").read[Boolean] and
      (JsPath \ "country").read[String]
    )(ProductEntity.apply _)
}

object TestApp extends App with HttpClient {
  println(signUp("test", "123"))
  println(signIn("test", "123"))
  println(getLoggedUser("d0075bd8985441dfab9980da97399ed6"))
//  print(getProducts("788994c56c584521b8c36827c921dcd4"))
}

case class UserResponse(id: Long, username: String, password: String, balance: Long, crossSaleBalance: Long)