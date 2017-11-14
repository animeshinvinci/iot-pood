package iot.pood.base.http.base

import akka.http.scaladsl.server.ExceptionHandler

/**
  * Created by rafik on 3.10.2017.
  */
trait ExceptionResolver {

  def exceptionHandler: ExceptionHandler

}
