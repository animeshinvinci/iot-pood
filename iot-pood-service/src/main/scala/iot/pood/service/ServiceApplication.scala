package iot.pood.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Route, StandardRoute}
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

  val apis = List(apiV1Route,apiV3Route)
  val mainRoute = joinRoute(apis)
  val bindingFuture = Http().bindAndHandle(mainRoute, httpConfig.host,httpConfig.port)
  log.info("Press any key to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => {
      log.info("Terminate ActorSystem")
      system.terminate()
      })


  def joinRoute(routes: List[Route]): Route = routes match {
        case List(last) => last
        case head :: tail => head ~ joinRoute(tail)
  }
}
