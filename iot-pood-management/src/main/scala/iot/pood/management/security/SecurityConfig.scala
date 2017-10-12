package iot.pood.management.security

import authentikat.jwt.JwtHeader
import com.typesafe.config.Config
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.log.Log

import scala.util.{Failure, Success, Try}

/**
  * Created by rafik on 11.10.2017.
  */
object SecurityConfig extends Log{

  object SecurityConstants{

    val security = "security"
    val expiration = "expiration"
    val secretKey = "secret_key"
    val header = "header"
  }


  def securityConfig(config: Config): SecurityConfig = {
    Try({
      val securityConfig = config.getConfig(SecurityConstants.security)
      val expiration = securityConfig.getInt(SecurityConstants.expiration)
      val secretKey = securityConfig.getString(SecurityConstants.secretKey)
      val header = securityConfig.getString(SecurityConstants.header)
      val jwtHeader = JwtHeader(header)
      SecurityConfig(expiration,secretKey,jwtHeader)
    }) match {
      case Success(c) => c
      case Failure(e) => {
        log.error("Unable to parse security configuration: ")
        throw new IncorrectConfigurationException(
          """
            |Example:
            |security {
            |  expiration = 15
            |  secret_key = "thisjusasodifsodifj"
            |  header = "HS256"
            |}
          """.stripMargin)
      }
    }


  }
}

case class SecurityConfig(expirationInMinutes: Int, secretKey: String, header: JwtHeader = JwtHeader("HS256"))
