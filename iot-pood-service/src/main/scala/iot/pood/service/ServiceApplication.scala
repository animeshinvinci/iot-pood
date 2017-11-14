package iot.pood.service

import akka.actor.Actor.Receive
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route, StandardRoute}
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import iot.pood.base.actors.BaseActor
import iot.pood.base.app.{ActorApp, ConfigurableApp, HttpApp}
import iot.pood.base.config.HttpConfig.HttpApiPrefix
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.http.base.ApiVersionService
import iot.pood.base.http.base.internal.HttpServiceCollector
import iot.pood.base.http.health.HealthHttpService
import iot.pood.base.log.Log
import iot.pood.service.service.simple.SimpleDataHttpService

import scala.concurrent.duration.DurationDouble
import scala.io.StdIn


/**
  * Created by rafik on 21.5.2017.
  */
object ServiceApplication extends App
  with ActorApp
  with HttpApp
  with ConfigurableApp
{
  startHttp
  override def httpServices: List[ApiVersionService] = {
    List(SimpleDataHttpService(),HealthHttpService())
  }
}
class SimpleActor extends BaseActor
{
  override def receive: Receive = {
    case _ => log.info("Just empty implementation")
  }

}

