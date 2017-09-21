package iot.pood.service.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
  * Created by rafik on 26.6.2017.
  */
trait JsonApi extends SprayJson
    with JsonDataRequest
    with JsonDataResponse
    with JsonManageRequest
    with JsonManageResponse
