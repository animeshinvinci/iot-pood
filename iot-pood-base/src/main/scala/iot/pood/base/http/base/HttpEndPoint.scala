package iot.pood.base.http.base

import akka.http.scaladsl.server.Route

/**
  * Created by rafik on 3.10.2017.
  */
trait HttpEndPoint {

  def route: Route

}
