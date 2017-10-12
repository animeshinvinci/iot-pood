package iot.pood.base.exception

/**
  * Common application exception
  *
  * Created by rafik on 7.9.2017.
  */
object Exceptions {

  case class IncorrectConfigurationException(message: String) extends RuntimeException(message)


  case class UnauthorizedException(message: String) extends RuntimeException(message)

  case class AuthenticationFailException(message: String) extends RuntimeException(message)

  case class TokenExpiredFailException(message: String) extends RuntimeException(message)
}
