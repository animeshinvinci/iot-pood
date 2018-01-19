package iot.pood.management

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.Config
import io.circe.generic.semiauto
import iot.pood.base.app.{ActorApp, ConfigurableApp, HttpApp}
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.http.base.{ApiVersionService, RouteJoin}
import iot.pood.base.http.base.internal.HttpServiceCollector
import iot.pood.base.http.health.HealthHttpService
import iot.pood.base.log.Log
import iot.pood.management.doc.{SwaggerDocService, SwaggerUi}
import iot.pood.management.http.device.DeviceHttpService
import iot.pood.management.http.group.GroupHttpService

import scala.concurrent.duration.DurationInt
import scala.io.StdIn

/**
  * Created by rafik on 12.9.2017.
  */
object ManagementApplication extends App
  with ActorApp
  with HttpApp
  with ConfigurableApp with RouteJoin{

  override def startHttp:Unit = {

    log.info("Start HTTP: {}:{}",httpConfig.host,httpConfig.port)
    var route = SwaggerDocService(httpConfig,system).routes ~ SwaggerUi.swaggerSiteRoute ~ httpRoute
    val bindingFuture = Http().bindAndHandle(route, httpConfig.host,httpConfig.port)
    log.info("Press any key to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        log.info("Terminate ActorSystem")
        system.terminate()
      })
  }

  def httpServices: List[ApiVersionService] = {
    List(
      GroupHttpService(),
      HealthHttpService(),
      DeviceHttpService()
    )
  }

  startHttp
//  import io.circe._
//  import io.circe.generic.auto._
//  import io.circe.parser._
//  import io.circe.syntax._
//
//  case class Device(name: String,value: Int)
//
//  val x = Device("name",10)
//
//  val ser = new SimpleSerializer[Device]
//  val des = new SimpleDeserializer[Device]
//
//  implicit val value1: Decoder[Device] = semiauto.deriveDecoder[Device]
//
//  val json = ser.serialize(x)
//  println(json)
//  val jsonObject:Either[Error, Device] = des.deserialize(json)
////  println(jsonObject)
//
//  println(decode[Device](json))
//  trait Serializer[T]
//  {
//    def serialize[T: Encoder](value: T):String
//  }
//
//  trait Deserializer[T]{
//    def deserialize[T: Decoder](value: String): Either[Error, T]
//  }
//
//  class SimpleSerializer[T] extends Serializer[T]{
//
//    override def serialize[T: Encoder](value: T): String = value.asJson.noSpaces
//  }
//
//  class SimpleDeserializer[T] extends Deserializer[T]{
//    override def deserialize[T: Decoder](value: String): Either[Error, T] = decode[T](value)
//  }

}
