package iot.pood.base.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{CollectionFormats, DefaultJsonProtocol, NullOptions}


/**
  * Created by rafik on 26.6.2017.
  */
trait SprayJson extends DefaultJsonProtocol with SprayJsonSupport
  with CollectionFormats
