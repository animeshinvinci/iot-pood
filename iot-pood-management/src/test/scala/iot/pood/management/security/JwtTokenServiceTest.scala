package iot.pood.management.security

import java.util.concurrent.TimeUnit

import iot.pood.base.model.security.SecurityMessages.{JwtToken, Token}
import iot.pood.base.model.user.UserMessages.SimpleUser
import iot.pood.management.base.BaseTest
import iot.pood.management.security.internal.JwtTokenService

/**
  * Created by rafik on 12.10.2017.
  */
class JwtTokenServiceTest extends BaseTest{


  "A jwt token service " should "create JWT token from unique user " in {
    val config = SecurityConfig(15,"secureKey")
    val service = JwtTokenService(config)
    val simpleUser = SimpleUser("123")

    val token: Token = service.create(simpleUser)
    token shouldBe a [JwtToken]

    token match {
      case JwtToken(jwt,_,_) => {
        val simpleUser = service.parse(jwt)
        simpleUser shouldBe a [SimpleUser]
        simpleUser.id should equal ("123")
      }
    }
  }
}
