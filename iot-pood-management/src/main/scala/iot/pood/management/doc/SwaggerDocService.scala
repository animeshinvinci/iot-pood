package iot.pood.management.doc

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.swagger.akka.{SwaggerHttpService, SwaggerSite}
import io.swagger.models.ExternalDocs
import io.swagger.models.auth.BasicAuthDefinition
import com.github.swagger.akka.SwaggerHttpService
import iot.pood.base.config.HttpConfig.HttpConfig
import iot.pood.management.http.device.DeviceHttpService

import scala.reflect.runtime.{universe => ru}

object SwaggerDocService  {

  def apply(config: HttpConfig,actorSystem: ActorSystem): SwaggerDocService = new SwaggerDocService(config,actorSystem)

}


class SwaggerDocService(config: HttpConfig,system: ActorSystem) extends SwaggerHttpService{

  override val host = "localhost:12345"
  override val externalDocs = Some(new ExternalDocs("Core Docs", "http://acme.com/docs"))
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())
  override val apiClasses = Set(classOf[DeviceHttpService])

  override val basePath = "/"    //the basePath for the API you are exposing
  override val apiDocsPath = "api-docs"
}

object SwaggerUi extends SwaggerSite

