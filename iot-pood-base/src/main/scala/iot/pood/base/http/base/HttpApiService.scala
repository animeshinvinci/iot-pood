package iot.pood.base.http.base

import akka.http.scaladsl.server.Directives

/**
  * Created by rafik on 3.10.2017.
  */
trait HttpApiService extends HttpEndPoint
  with RouteJoin
  with Directives