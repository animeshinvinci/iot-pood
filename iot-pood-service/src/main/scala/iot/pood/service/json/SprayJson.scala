package iot.pood.service.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json._

/**
  * Created by rafik on 26.6.2017.
  */
trait SprayJson extends SprayJsonSupport with DefaultJsonProtocol {

}
