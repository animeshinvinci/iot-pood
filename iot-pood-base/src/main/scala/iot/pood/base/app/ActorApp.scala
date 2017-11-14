package iot.pood.base.app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.http.base.internal.HttpServiceCollector
import iot.pood.base.http.health.HealthHttpService

import scala.concurrent.duration.DurationInt
import scala.io.StdIn

/**
  * Created by rafik on 8.11.2017.
  */
trait ActorApp {
  implicit val system = ActorSystem()
  implicit val timeout = Timeout(2.seconds)
}
