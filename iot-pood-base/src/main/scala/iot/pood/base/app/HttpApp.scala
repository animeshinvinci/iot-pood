package iot.pood.base.app

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.http.base.ApiVersionService
import iot.pood.base.http.base.internal.HttpServiceCollector
import iot.pood.base.http.health.HealthHttpService
import iot.pood.base.log.Log

import scala.io.StdIn

/**
  * Created by rafik on 8.11.2017.
  */
trait HttpApp extends ConfigurableApp with Log{
  this: ActorApp =>

  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  lazy val httpConfig = HttpConfig.httpConfig(config)

  def startHttp:Unit = {

    log.info("Start HTTP: {}:{}",httpConfig.host,httpConfig.port)
    val bindingFuture = Http().bindAndHandle(httpRoute
      , httpConfig.host,httpConfig.port)
    log.info("Press any key to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        log.info("Terminate ActorSystem")
        system.terminate()
      })
  }


  def httpRoute: Route =  HttpServiceCollector(httpConfig,httpServices).route

  def httpServices: List[ApiVersionService]

}
