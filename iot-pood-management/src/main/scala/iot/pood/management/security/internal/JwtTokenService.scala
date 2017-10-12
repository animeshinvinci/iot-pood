package iot.pood.management.security.internal

import java.util.UUID
import java.util.concurrent.TimeUnit

import authentikat.jwt.{JsonWebToken, JwtClaimsSet}
import iot.pood.base.exception.Exceptions.AuthenticationFailException
import iot.pood.base.log.Log
import iot.pood.base.model.security.SecurityMessages.{JwtToken, Token}
import iot.pood.base.model.user.UserMessages.{SimpleUser, User}
import iot.pood.management.security.{SecurityConfig, TokenService}

/**
  * Created by rafik on 11.10.2017.
  */
object JwtTokenService {

  val USER = "user"
  val EXPIRED_AT = "expired_at"

  def apply(securityConfig: SecurityConfig): JwtTokenService = new JwtTokenService(securityConfig)

  class JwtTokenService(securityConfig: SecurityConfig) extends TokenService with Log{


    override def parse(token: String): User = getClaims(token) match {
      case Some(data) => {
        (data.get(EXPIRED_AT),data.get(USER)) match {
          case (Some(expiration),Some(user)) if isExpirationValid(expiration.toLong) => {
            val simpleUser = SimpleUser(user)
            log.info("Token parse to user: {}",simpleUser)
            simpleUser
          }
          case (Some(expiration),None) if isExpirationValid(expiration.toLong) => {
            log.error("Claims is not presented !!!")
            throw new AuthenticationFailException("Claims is not presented !!!")
          }
          case (Some(expiration),_) if !isExpirationValid(expiration.toLong) => {
            log.error("Token expired")
            throw new AuthenticationFailException("Token expired !!!")
          }
        }
      }
      case None => throw new AuthenticationFailException("Claims is not presented !!!")
    }

    override def create(user: User): Token = {
        val jwt = JsonWebToken(securityConfig.header,
                    createClaims(user,securityConfig),
          securityConfig.secretKey
        )
      JwtToken(jwt,securityConfig.expirationInMinutes,UUID.randomUUID().toString)
    }


    private def getClaims(jwtToken: String) = jwtToken match {
      case JsonWebToken(_,claims,_) => claims.asSimpleMap.toOption
      case _ => None
    }

    private def createClaims(user: User,securityConfig: SecurityConfig) = JwtClaimsSet{
      Map(USER -> user.id,
        EXPIRED_AT -> (System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(securityConfig.expirationInMinutes.toLong)))
    }

    private def isExpirationValid(expiration: Long) = expiration > System.currentTimeMillis()
  }
}



