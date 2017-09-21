package iot.pood.service.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import iot.pood.service.json.JsonApi
import iot.pood.service.rest.v1.DataSimpleApi

/**
  * Created by rafik on 23.7.2017.
  */
trait ApiV1 extends JsonApi with DataSimpleApi{

  def apiV1Route: Route = {
      pathPrefix("api"/"v1"){
        dataSimple
      }
  }
}
