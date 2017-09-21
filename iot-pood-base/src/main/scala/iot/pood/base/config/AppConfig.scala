package iot.pood.base.config

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by rafik on 31.7.2017.
  */
trait AppConfig {

  def config: Config = Configuration.appConfig

}

