package iot.pood.management

import iot.pood.base.app.{ActorApp, ConfigurableApp, HttpApp}
import iot.pood.base.http.base.ApiVersionService
import iot.pood.base.http.health.HealthHttpService

import scala.io.StdIn

/**
  * Created by rafik on 12.9.2017.
  */
object ManagementApplication extends App with ActorApp
  with HttpApp
  with ConfigurableApp {

  override def httpServices: List[ApiVersionService] = List(HealthHttpService())

  startHttp
}
