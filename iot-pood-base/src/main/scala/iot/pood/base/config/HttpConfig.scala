package iot.pood.base.config

import com.typesafe.config.Config
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.log.Log

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}

/**
  * Created by rafik on 21.9.2017.
  */
object HttpConfig extends Log{

  object HttpConstants {

    val MAIN = "http"
    val HOST = "host"
    val PORT = "port"
    val API_URL = "api-url"
    val API_VERSIONS = "api-versions"
  }

  def httpConfig(config: Config) = {
    Try({
      val http = config.getConfig(HttpConstants.MAIN)
      val versions = http.getStringList(HttpConstants.API_VERSIONS).asScala.toList
      HttpConfig(http.getString(HttpConstants.HOST),
        http.getInt(HttpConstants.PORT),
        http.getString(HttpConstants.API_URL),
        versions,
        apiConfig(versions,http)
        )
    }) match {
      case Success(x) => x
      case Failure(_) => {
        throw new IncorrectConfigurationException(
          """
            |Unable to parse http configuration section !!!
            |Example:
            | http {
            |    host = "0.0.0.0"
            |    port = 8082
            |    api-url = "api"
            |    api-versions = ["v1","v2"]
            |    v1 = {}
            |    v2 = {}
            |}
          """.stripMargin)
      }
    }
  }


  private def apiConfig(names: List[String],config: Config) = {
    Map(names map { name => (name,config.getConfig(name))} : _*)
  }

  case class HttpConfig(host: String, port: Int, apiUrl: String, apiVersions: List[String],
                           apiConfig: Map[String,Config])

  case class HttpApiPrefix(mainUrl: String, version: String)

}
