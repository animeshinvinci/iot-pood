package iot.pood.service.rest.v1

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import iot.pood.service.json.JsonApi

/**
  * Created by rafik on 23.7.2017.
  */
trait DataSimpleApi {
  this: JsonApi =>

  def dataSimple: Route = {

    get {
      pathPrefix("data" / "simple") {
        pathPrefix(DoubleNumber) { value => {
            complete(StatusCodes.OK,"ok")
          }
        } ~
          pathPrefix("on") {
            complete("on")
          } ~
          pathPrefix("off") {
            complete("of")
          }
      }
    }
  }
}
