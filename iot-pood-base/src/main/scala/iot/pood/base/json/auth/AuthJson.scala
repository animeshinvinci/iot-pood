package iot.pood.base.json.auth

import iot.pood.base.json.support.circle.CircleJson

/**
  * Created by rafik on 11.10.2017.
  */
object AuthJson {

  case class LoginRequest(login: String, password: String)

  case class TokenAuthResponse(token_type:String,
                               access_token: String,
                               expires_in: Int,
                               refresh_token: String)


}

trait AuthJson extends CircleJson {

}


