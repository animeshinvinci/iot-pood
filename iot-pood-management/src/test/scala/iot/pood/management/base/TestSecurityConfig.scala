package iot.pood.management.base

import com.typesafe.config.ConfigFactory
import iot.pood.management.security.SecurityConfig

/**
  * Created by rafik on 12.10.2017.
  */
trait TestSecurityConfig {

  private def config = {
   ConfigFactory.parseString(
      """
        |security {
        |  expiration = 2 minutes
        |  secret_key = "thisjusasodifsodifj"
        |  header = "HS256"
        |}
      """.stripMargin)
  }

  def securityConfig = {
    SecurityConfig.securityConfig(config)
  }
}
