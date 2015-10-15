package controllers

import java.util.Base64

import com.ning.http.client.AsyncHttpClient

trait HttpClient {
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

    if (response.nonEmpty) Some(response) else None
  }

  def signUp(login: String, password: String): Option[String] = {
    val response = client
      .preparePost(host + "/auth/signUp")
      .setBody(s"""{"username":"${login}","password":"${password}"}""")
      .addHeader("Content-Type", "application/json")
      .execute()
      .get()
      .getResponseBody("UTF-8")

    if (response.nonEmpty) Some(response) else None
  }
}

object TestApp extends App with HttpClient {
  println(signUp("test", "123"))
  println(signIn("test", "123"))
}