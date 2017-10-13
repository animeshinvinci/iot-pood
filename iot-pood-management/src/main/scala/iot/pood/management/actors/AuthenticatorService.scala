package iot.pood.management.actors

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.base.exception.Exceptions.{AuthenticationFailException, TokenExpiredFailException}
import iot.pood.base.model.security.SecurityMessages.{JwtToken, Token}
import iot.pood.base.model.user.UserMessages.{SimpleUser, User}
import iot.pood.management.security.TokenService

import scala.util.{Failure, Success, Try}

/**
  * Created by rafik on 12.10.2017.
  */
object AuthenticatorService {

  val NAME = "authenticator"

  def props(tokenService: TokenService[JwtToken]): Props = Props(new AuthenticatorServiceActor(tokenService))

  sealed trait AuthMessages
  case class LoginRequest(login: String, password: String) extends AuthMessages
  case class LogoutRequest(refreshToken: String) extends AuthMessages
  case class LoginSuccessResponse(token: Token) extends AuthMessages
  object LoginErrorResponse extends AuthMessages

  object LogoutSuccessResponse extends AuthMessages
  object LogoutErrorResponse extends AuthMessages

  case class AuthenticationRequest(token: String) extends AuthMessages
  case class AuthenticationSuccess(user: User) extends AuthMessages
  object TokenExpiredResponse extends AuthMessages
  object InvalidTokenResponse extends AuthMessages
  object UnauthorizedResponse extends AuthMessages
  object AuthErrorResponse extends AuthMessages
}

class AuthenticatorServiceActor(tokenService: TokenService[JwtToken]) extends BaseActor {

  import AuthenticatorService._
  import iot.pood.base.model.security.SecurityMessages._

  override def receive: Receive = {
    case LoginRequest(user,"user") => {
      tokenService.create(SimpleUser(user)) match {
        case token: JwtToken => {
          log.info("User: {} login OK. Token: {}",user,token)
          sender() ! LoginSuccessResponse(token)
        }
        case _ => sender() !LoginErrorResponse
      }
    }
    case LoginRequest(_,"incorrect") => sender() !LoginErrorResponse
    case LogoutRequest => sender() ! LogoutSuccessResponse


    case AuthenticationRequest(token) => {
      log.info("Authentication for token: {}",token)
      tokenService.parse(token) match {
        case Success(user) => {
          user.id match {
            case "admin" =>  {
              log.info("Success authorize user: {}",user)
              sender() ! AuthenticationSuccess(user)
            }
            case "user1" => {
              log.info("Unauthorized user: {}",user)
              sender() ! UnauthorizedResponse
            }
          }
        }
        case Failure(e: AuthenticationFailException) => {
          log.error("Token is invalid: {}",token)
          sender() ! InvalidTokenResponse
        }
        case Failure(e: TokenExpiredFailException) => {
          log.error("Token expired: {}",token)
          sender() ! TokenExpiredResponse
        }
        case Failure(_) => {
          log.error("Internal error problem ")
          sender() ! AuthErrorResponse
        }
      }
    }
  }
}

