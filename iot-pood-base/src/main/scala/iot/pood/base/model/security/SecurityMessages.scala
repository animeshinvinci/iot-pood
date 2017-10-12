package iot.pood.base.model.security


/**
  * Created by rafik on 11.10.2017.
  */
object SecurityMessages {


  sealed trait Token

  case class JwtToken(authToken: String, expiration: Int,
                   refreshToken: String) extends Token


}
