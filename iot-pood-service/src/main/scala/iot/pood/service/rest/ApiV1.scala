package iot.pood.service.rest

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import iot.pood.base.json.JsonApi
import iot.pood.base.json.exception.ErrorMessages
import iot.pood.base.json.health.HealthMessages

/**
  * Created by rafik on 23.7.2017.
  */
trait ApiV1 extends JsonApi
  with ErrorMessages
  with HealthMessages {

  val prefix = "api"
  val version = "v1"

  def apiV1Route: Route = {
    pathPrefix(prefix/version){
      pathPrefix("on"){
        complete{
          HealthResponse(1,None,None,Map("1"->AppParameter("1","1")))
        }
      }
    }
  }

  def apiV3Route: Route = {
    pathPrefix("api"/"v3"){
      complete("ok")
    }
  }

}
