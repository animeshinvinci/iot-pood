package iot.pood.service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import iot.pood.service.rest.ApiV1

import scala.io.StdIn


/**
  * Created by rafik on 21.5.2017.
  */
object ServiceApplication extends App with ApiV1 {


  val config = ConfigFactory.load()

  println("This is applicasdftion")
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val port = config.getInt("http.port")

  val bindingFuture = Http().bindAndHandle(apiV1Route, "0.0.0.0", port)

  println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
