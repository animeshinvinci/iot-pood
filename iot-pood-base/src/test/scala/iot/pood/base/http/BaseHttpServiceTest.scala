package iot.pood.base.http

import akka.actor.ActorSystem
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import iot.pood.base.actors.BaseActorTest
import iot.pood.base.app.{ActorApp, ConfigurableApp, HttpApp}
import org.scalatest.{FlatSpec, Matchers, WordSpec}
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{HttpResponse, HttpRequest}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

/**
  * Created by rafik on 9.11.2017.
  */
abstract class BaseHttpServiceTest  extends FlatSpec with Matchers with ScalatestRouteTest {


}
