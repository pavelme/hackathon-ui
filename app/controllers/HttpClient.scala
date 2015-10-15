package controllers

import java.util.Base64

import com.ning.http.client.AsyncHttpClient
import models.{User, TokenEntity}
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
}

object TestApp extends App with HttpClient {
  println(signUp("test", "123"))
  println(signIn("test", "123"))
  println(getLoggedUser("d0075bd8985441dfab9980da97399ed6"))
}

case class UserResponse(id: Long, username: String, password: String, balance: Long, crossSaleBalance: Long)