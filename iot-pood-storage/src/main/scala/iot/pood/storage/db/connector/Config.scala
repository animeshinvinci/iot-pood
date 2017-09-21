package iot.pood.storage.db.connector

import com.typesafe.config.ConfigFactory

/**
  * Created by rafik on 7.8.2017.
  */
trait AppConfig {

  def appConfig = ConfigFactory.load


}
