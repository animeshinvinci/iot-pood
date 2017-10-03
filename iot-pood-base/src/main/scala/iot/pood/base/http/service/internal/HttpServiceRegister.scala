package iot.pood.base.http.service.internal

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import iot.pood.base.config.HttpConfig.{HttpApiPrefix, HttpConfig}
import iot.pood.base.http.service.{ExceptionResolver, HttpApiService}

import scala.concurrent.ExecutionContext

/**
  * Created by rafik on 3.10.2017.
  */

object HttpServiceRegister{
  def apply(httpApi: HttpApiPrefix,
            httpServices: List[HttpApiService],
        exceptionHandler: ExceptionHandler
  ) ( implicit executorContext: ExecutionContext): HttpServiceRegister = new HttpServiceRegister(httpApi, httpServices,exceptionHandler)
}

class HttpServiceRegister(httpApi: HttpApiPrefix,
                          httpServices: List[HttpApiService],
                           val exceptionHandler: ExceptionHandler)(implicit executorContext: ExecutionContext) extends HttpApiService with ExceptionResolver{


  override def route: Route = handleExceptions(exceptionHandler){
      pathPrefix(httpApi.mainUrl/httpApi.version){
        joinRoute(httpServices.map(service => service.route))
      }
  }
}
