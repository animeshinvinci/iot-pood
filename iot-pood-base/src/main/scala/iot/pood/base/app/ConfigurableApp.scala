package iot.pood.base.app

import iot.pood.base.config.Configuration
import com.typesafe.config.Config
import iot.pood.base.log.Log

/**
  *
  *
  * Created by rafik on 8.11.2017.
  */
trait ConfigurableApp {

  def config: Config = {
    Configuration.init()
    Configuration.appConfig
  }

}
