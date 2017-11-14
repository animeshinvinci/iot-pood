package iot.pood.base.http.base

import akka.http.scaladsl.server._

/**
  * Created by rafik on 3.10.2017.
  */
trait RouteJoin extends RouteConcatenation{

  def joinRoute(routes: List[Route]): Route = routes match {
    case List(last) => last
    case first :: tail => first ~ joinRoute(tail)
  }

  def join(routes: Route *): Route = {
    joinRoute(List(routes: _*))
  }
}
