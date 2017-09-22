package iot.pood.base.config

import com.typesafe.config.Config

/**
  * Created by rafik on 22.9.2017.
  */
trait Configurator[A] {

  def appConfig: Config

  def componentConfig: Config

  def get(name: String):A

}
