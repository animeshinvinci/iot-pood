package iot.pood.base.json.auth

import iot.pood.base.json.JsonApi

/**
  * Created by rafik on 11.10.2017.
  */
trait AuthMessages {
  this: JsonApi =>

  case class LoginRequest(login: String, password: String)

  case class TokenAuthResponse(token_type:String,
                               access_token: String,
                               expires_in: Int,
                               refresh_token: String)


  implicit val loginRequestFormatter = jsonFormat2(LoginRequest)
  implicit val tokenAuthResponseFormatter=  jsonFormat4(TokenAuthResponse)

}
