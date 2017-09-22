package iot.pood.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import iot.pood.base.config.Configuration
import iot.pood.base.http.HttpConfig
import iot.pood.base.log.Log
import iot.pood.service.rest.ApiV1

import scala.io.StdIn


/**
  * Created by rafik on 21.5.2017.
  */
object ServiceApplication extends App with ApiV1 with Log{

  Configuration.init()
  val httpConfig = HttpConfig.httpConfig(Configuration.appConfig)

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher


  log.info("Start server HOST: {}:{} ",httpConfig.host,httpConfig.port)
  log.info("HTTP configuration: {}",httpConfig)
  val bindingFuture = Http().bindAndHandle(apiV1Route, httpConfig.host,httpConfig.port)
  log.info("Press any key to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => {
      log.info("Terminate ActorSystem")
      system.terminate()
      })
}
