package iot.pood.base.http.base.internal

import akka.actor.ActorSystem
import akka.http.javadsl.server.{Rejection, Rejections}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, _}
import akka.http.scaladsl.server._
import iot.pood.base.config.HttpConfig.{HttpApiPrefix, HttpConfig}
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.http.base.{ApiVersionService, ExceptionResolver, HttpApiService}
import iot.pood.base.log.Log
import org.jboss.netty.handler.codec.http.HttpVersion

import scala.concurrent.ExecutionContext

/**
  * Common object for collection IoT - pood rest API with default ExceptionHandler
  *
  * Created by rafik on 3.10.2017.
  */
class HttpServiceCollector(httpConfig: HttpConfig,
                           httpServices: List[ApiVersionService],
                           val exceptionHandler: ExceptionHandler,
                           rejectionHandler: RejectionHandler)(implicit executorContext: ExecutionContext) extends HttpApiService with ExceptionResolver
   with Log{


  override def route: Route = {
    handleExceptions(exceptionHandler){
      handleRejections(rejectionHandler) {
        joinRoute(collectVersionedApi(httpConfig, httpServices))
      }
    }
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
          complete(HttpResponse(StatusCodes.InternalServerError))
        }
    }
  }

  private[this] val rejectionHandler =
    RejectionHandler.newBuilder()
      .handle {
        case RequestEntityExpectedRejection => complete(HttpResponse(StatusCodes.InternalServerError,entity = "error1"))
        case MalformedRequestContentRejection(msg,e) => complete(HttpResponse(StatusCodes.InternalServerError,entity = "error2"))
        case UnsupportedRequestContentTypeRejection(supported) => complete(HttpResponse(StatusCodes.InternalServerError,entity = "error3"))
        case _ => {
          complete(HttpResponse(StatusCodes.InternalServerError,entity = "error4"))
        }
      }
      .handleNotFound{
        complete(HttpResponse(StatusCodes.NotFound))
      }
      .result()


  def apply(httpConfig: HttpConfig,
            httpServices: List[ApiVersionService],
        exceptionHandler: ExceptionHandler = defaultExceptionHandler,
        rejectionHandler: RejectionHandler = rejectionHandler
  ) ( implicit executorContext: ExecutionContext): HttpServiceCollector = new HttpServiceCollector(httpConfig, httpServices,exceptionHandler,rejectionHandler)

}
