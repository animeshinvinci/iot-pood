package iot.pood.storage

import java.util.Calendar

import akka.actor.{ActorRef, ActorSystem, Props}
import iot.pood.base.app.{ActorApp, ConfigurableApp, HttpApp}
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.http.base.ApiVersionService
import iot.pood.base.http.health.HealthHttpService
import iot.pood.base.integration.IntegrationConfig
import iot.pood.base.log.Log
import iot.pood.base.messages.integration.ComponentMessages.{GetIntegrationComponentRequest, GetIntegrationComponentResponse}
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ProducerRequest, ProducerSend, RegisterDataListener}
import iot.pood.integration.util.{SimpleCommandUtilPublisher, SimpleDataUtilPublisher}
import iot.pood.storage.actor.StorageAppGuardian

import scala.io.StdIn
import scala.concurrent.{Await, Future}

/**
  * Created by rafik on 4.8.2017.
  */
object StorageApplication extends App with ActorApp
    with HttpApp
    with ConfigurableApp
{

  override def httpServices: List[ApiVersionService] = List(HealthHttpService())
//  log.info("Application start")
//  Configuration.init()
//  val httpConfig = HttpConfig.httpConfig(Configuration.appConfig)
//  val integrationConfig = IntegrationConfig(Configuration.appConfig)
//
//  val system = ActorSystem()
//  val mainActor = system.actorOf(StorageAppGuardian.props(integrationConfig),StorageAppGuardian.NAME)
//  val simpleCommandPublisher = system.actorOf(SimpleCommandUtilPublisher.props(mainActor),SimpleCommandUtilPublisher.NAME)
//  val simpleDataPublisher = system.actorOf(SimpleDataUtilPublisher.props(mainActor),SimpleDataUtilPublisher.NAME)
//
//  log.info("Press ANY key to stop...")
//  StdIn.readLine()
//  system.terminate()
//  log.info("Application stop")

  startHttp
}

