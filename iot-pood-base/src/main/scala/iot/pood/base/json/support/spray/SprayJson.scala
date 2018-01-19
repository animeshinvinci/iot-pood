package iot.pood.base.json.support.spray

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{CollectionFormats, DefaultJsonProtocol}

trait SprayJson extends DefaultJsonProtocol with SprayJsonSupport
  with CollectionFormats