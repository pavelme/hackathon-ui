package models

case class User(id: Long, username: String, password: String, balance: Long, crossSellBalance: Long)
