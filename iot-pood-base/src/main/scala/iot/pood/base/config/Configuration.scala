package iot.pood.base.config

import com.typesafe.config.{Config, ConfigFactory}
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.log.Log

/**
  * Created by rafik on 21.9.2017.
  */
object Configuration extends Log{

  private[this] var globalConfig:Option[Config] = None

  def init() = {
    log.info("Init application config")
    globalConfig match {
      case None => globalConfig = Some(ConfigFactory.load())
      case Some(_) => log.info("Configuration object is already set !!!")
    }
  }

  def appConfig(): Config = {
    globalConfig match {
      case None => throw new IncorrectConfigurationException("Config object is not initialize !!!. First call init method")
      case Some(x) => x
    }
  }
}
