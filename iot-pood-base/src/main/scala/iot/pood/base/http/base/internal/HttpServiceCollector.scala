package iot.pood.base.http.base.internal

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import iot.pood.base.config.HttpConfig.{HttpApiPrefix, HttpConfig}
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.http.base.{ApiVersionService, ExceptionResolver, HttpApiService}
import org.jboss.netty.handler.codec.http.HttpVersion

import scala.concurrent.ExecutionContext

/**
  * Common object for collection IoT - pood rest API with default ExceptionHandler
  *
  * Created by rafik on 3.10.2017.
  */
class HttpServiceCollector(httpConfig: HttpConfig,
                           httpServices: List[ApiVersionService],
                           val exceptionHandler: ExceptionHandler)(implicit executorContext: ExecutionContext) extends HttpApiService with ExceptionResolver{

  override def route: Route = handleExceptions(exceptionHandler){
    joinRoute(collectVersionedApi(httpConfig,httpServices))
  }

  def collectVersionedApi(httpConfig: HttpConfig,httpServices: List[ApiVersionService]) = {

    httpServices.groupBy(service => service.version).map(k=>{
      httpConfig.apiConfig.get(k._1) match {
        case Some(api) => createRoute(HttpApiPrefix(httpConfig.apiUrl,k._1),httpServices)
        case None => {
          throw new IncorrectConfigurationException(
            s"""
              |Incorrect API configuration
              |Actual is: ${k._1} but only: ${httpConfig.apiVersions} are supported !!!
            """.stripMargin)
        }
      }
    }).toList
  }

  def createRoute(httpApi: HttpApiPrefix,services: List[ApiVersionService]) = {
    pathPrefix(httpApi.mainUrl/httpApi.version){
      joinRoute(services.map(service => service.route))
    }
  }

}

object HttpServiceCollector{

  private[this] val defaultExceptionHandler: ExceptionHandler = ExceptionHandler {
    case _: Exception =>
      extractUri { uri =>
        extractRequest { r =>
          println(s"Request to $uri could not be handled normally")
          complete(HttpResponse(StatusCodes.InternalServerError, entity = "Bad numbers, bad result!!!"))
        }
    }
  }


  def apply(httpConfig: HttpConfig,
            httpServices: List[ApiVersionService],
        exceptionHandler: ExceptionHandler = defaultExceptionHandler
  ) ( implicit executorContext: ExecutionContext): HttpServiceCollector = new HttpServiceCollector(httpConfig, httpServices,exceptionHandler)

}
