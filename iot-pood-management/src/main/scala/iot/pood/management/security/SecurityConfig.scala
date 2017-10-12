package iot.pood.management.security

import authentikat.jwt.JwtHeader
import com.typesafe.config.Config
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.log.Log

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
  * Created by rafik on 11.10.2017.
  */
object SecurityConfig extends Log{

  import scala.concurrent.duration._

  object SecurityConstants{

    val security = "security"
    val expiration = "expiration"
    val secretKey = "secret_key"
    val header = "header"
  }


  def securityConfig(config: Config): SecurityConfig = {
    Try({
      val securityConfig = config.getConfig(SecurityConstants.security)
      val expiration = securityConfig.getString(SecurityConstants.expiration)
      val secretKey = securityConfig.getString(SecurityConstants.secretKey)
      val header = securityConfig.getString(SecurityConstants.header)
      val jwtHeader = JwtHeader(header)
      SecurityConfig(createDuration(expiration),secretKey,jwtHeader)
    }) match {
      case Success(c) => c
      case Failure(e) => {
        log.error("Unable to parse security configuration: {}",e.getMessage)
        throw new IncorrectConfigurationException(
          """
            |Example:
            |security {
            |  expiration = 15 minutes
            |  secret_key = "thisjusasodifsodifj"
            |  header = "HS256"
            |}
          """.stripMargin)
      }
    }
  }


  def createDuration(duration: String):Duration ={
    duration.split(" ") match {
      case Array(value,timeDuration) => Duration(duration)
      case _ => throw new IncorrectConfigurationException(
        """
          |Unable to parse duration config:
          |Example:
          |expiration = 15 minutes
        """.stripMargin)
    }
  }



}

case class SecurityConfig(expiration: Duration, secretKey: String, header: JwtHeader = JwtHeader("HS256"))
