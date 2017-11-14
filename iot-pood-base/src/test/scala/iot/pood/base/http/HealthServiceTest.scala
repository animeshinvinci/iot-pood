package iot.pood.base.http

import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.scaladsl.Flow
import iot.pood.base.http.health.HealthHttpService
import org.scalatest._


/**
  * Created by rafik on 9.11.2017.
  */
class HealthServiceTest extends BaseHttpServiceTest {

  lazy val healthRoute = HealthHttpService().route

  "Health service" should "return OK" in {
      Get("/health/check") ~> healthRoute ~> check{
        status should ===(OK)
        responseAs[String] should ===("OK")
      }
  }
}
