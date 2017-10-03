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
import iot.pood.base.config.HttpConfig.HttpApiPrefix
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.http.service.internal.HttpServiceRegister
import iot.pood.base.log.Log
import iot.pood.service.rest.ApiV1
import iot.pood.service.service.simple.SimpleDataHttpService

import scala.concurrent.duration.DurationDouble
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
  implicit val timeout = Timeout(2.seconds)

  log.info("Start server HOST: {}:{} ",httpConfig.host,httpConfig.port)
  log.info("HTTP configuration: {}",httpConfig)
  val actor = system.actorOf(Props(new SimpleActor))

  val myExceptionHandler = ExceptionHandler {
    case _: Exception =>
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally")
        complete(HttpResponse(StatusCodes.InternalServerError, entity = "Bad numbers, bad result!!!"))
      }
  }

  val mainRoute =  HttpServiceRegister(HttpApiPrefix("api","v1"),List(SimpleDataHttpService(actor)),myExceptionHandler).route

  val bindingFuture = Http().bindAndHandle(mainRoute, httpConfig.host,httpConfig.port)
  log.info("Press any key to stop...")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => {
      log.info("Terminate ActorSystem")
      system.terminate()
      })
}


class SimpleActor extends BaseActor
{
  override def receive: Receive = {
    case _ => log.info("Just empty implementation")
  }

}

