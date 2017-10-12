package iot.pood.management.actor

import akka.testkit.TestProbe
import iot.pood.base.actors.BaseActorTest
import iot.pood.base.model.security.SecurityMessages.JwtToken
import iot.pood.management.actors.AuthenticatorService
import iot.pood.management.actors.AuthenticatorService.{LoginErrorResponse, LoginRequest, LoginSuccessResponse}
import iot.pood.management.base.TestSecurityConfig
import iot.pood.management.security.internal.JwtTokenService

/**
  * Created by rafik on 12.10.2017.
  */
class AuthenticatorServiceTest extends SecurityTests {

  "Authenticator service actor " must {
    "with credential (user,user) return success login response" in {
      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(securityConfig)))
      authenticatorService ! LoginRequest("user", "user")
      expectMsgPF(){
        case LoginSuccessResponse(x: JwtToken) => {
          x should not be (Nil)
          x.expiration should === (2)
          x.refreshToken should not be (Nil)
          x.authToken should not be (Nil)
        }
      }
    }
    "with credetial (user,incorrect) return error login response" in {

      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(securityConfig)))
      authenticatorService ! LoginRequest("user", "incorrect")
      expectMsg(LoginErrorResponse)
    }
  }
}
