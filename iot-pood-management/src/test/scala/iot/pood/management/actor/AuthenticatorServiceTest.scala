package iot.pood.management.actor

import iot.pood.base.model.security.SecurityMessages.JwtToken
import iot.pood.management.actors.AuthenticatorService
import iot.pood.management.actors.AuthenticatorService._
import iot.pood.management.security.internal.JwtTokenService
import akka.pattern.ask
import com.typesafe.config.ConfigFactory
import iot.pood.base.model.user.UserMessages.SimpleUser
import iot.pood.management.security.SecurityConfig

import scala.concurrent.Await
import scala.concurrent.duration.DurationDouble
/**
  * Created by rafik on 12.10.2017.
  */
class AuthenticatorServiceTest extends SecurityTests {


  "Authenticator service actor " must {
    "with credential (user,user) return success login response" in {
      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(securityConfig)), AuthenticatorService.NAME)
      authenticatorService ! LoginRequest("user", "user")
      expectMsgPF() {
        case LoginSuccessResponse(x: JwtToken) => {
          x should not be (Nil)
          x.expiration should ===(2)
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

    "login success with credential (admin,user)" in {
      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(securityConfig)))
      val futureResult = authenticatorService ? LoginRequest("admin", "user")
      val result = Await.result(futureResult, 1 seconds).asInstanceOf[LoginSuccessResponse]
      result.token match {
        case token: JwtToken => {
          val authFuture = authenticatorService ? AuthenticationRequest(token.authToken)
          val authResult = Await.result(authFuture, 1 seconds).asInstanceOf[AuthenticationSuccess]
          authResult.user should matchPattern {
            case SimpleUser("admin") =>
          }
        }
      }
    }
    "login success with credential (user1,user) but get Unauthorized message" in {
      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(securityConfig)))
      val futureResult = authenticatorService ? LoginRequest("user1", "user")
      val result = Await.result(futureResult, 1 seconds).asInstanceOf[LoginSuccessResponse]
      result.token match {
        case token: JwtToken => {
          authenticatorService ! AuthenticationRequest(token.authToken)
          expectMsg(UnauthorizedResponse)
        }
      }
    }

    "login success with credential (admin,user) but after that access with expired token" in {

      val expiredConfig = ConfigFactory.parseString(
        """
          |security {
          |  expiration = 2 seconds
          |  secret_key = "thisjusasodifsodifj"
          |  header = "HS256"
          |}
        """.stripMargin)


      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(SecurityConfig.securityConfig(expiredConfig))))
      authenticatorService ! LoginRequest("admin", "user")
      expectMsgPF() {
        case LoginSuccessResponse(token: JwtToken) => {
          Thread.sleep(3000)
          authenticatorService ! AuthenticationRequest(token.authToken)
          expectMsg(TokenExpiredResponse)
        }
      }
    }
    "invalid token authentication" in {
      val authenticatorService = system.actorOf(AuthenticatorService.props(JwtTokenService(securityConfig)))
      authenticatorService ! AuthenticationRequest("123")
      expectMsg(InvalidTokenResponse)
    }
  }
}
