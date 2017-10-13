package iot.pood.management.security

import java.util.concurrent.TimeUnit

import iot.pood.base.exception.Exceptions.TokenExpiredFailException
import iot.pood.base.model.security.SecurityMessages.{JwtToken, Token}
import iot.pood.base.model.user.UserMessages.SimpleUser
import iot.pood.management.base.BaseTest
import iot.pood.management.security.internal.JwtTokenService

import scala.concurrent.duration
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
  * Created by rafik on 12.10.2017.
  */
class JwtTokenServiceTest extends BaseTest{


  "JWT token service " should "create JWT token from unique user and be valid" in {
    val config = SecurityConfig(Duration("15 minutes"),"secureKey")
    val service = JwtTokenService(config)
    val simpleUser = SimpleUser("123")

    val token: Token = service.create(simpleUser)
    token shouldBe a [JwtToken]

    token match {
      case JwtToken(jwt,_,_) => {
        val simpleUser = service.parse(jwt)
        simpleUser match {
          case Success(user)=> {
            user shouldBe a [SimpleUser]
            user.id should equal ("123")
          }
          case Failure(_) =>
        }
      }
    }
  }

  it should "invalidate token and throw TokenExpiredFailException " in {
    val config = SecurityConfig(Duration(2,duration.SECONDS),"secureKey")
    val service = JwtTokenService(config)
    val simpleUser = SimpleUser("123")

    val token: Token = service.create(simpleUser)
    token shouldBe a [JwtToken]
    Thread.sleep(3000)
    token match {
      case JwtToken(jwt,_,_) => {
        a [TokenExpiredFailException] should be thrownBy{
          service.parse(jwt)
        }
      }
    }
  }
}
