package iot.pood.rules

import java.time.Duration
import java.util.Calendar

import akka.actor.{ActorRef, ActorSystem, Props}
import iot.pood.base.config.{Configuration, HttpConfig}
import iot.pood.base.integration.IntegrationConfig
import iot.pood.base.log.Log
import iot.pood.base.messages.integration.ComponentMessages.{GetIntegrationComponentRequest, GetIntegrationComponentResponse}
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ProducerRequest, ProducerSend, RegisterDataListener}
import iot.pood.integration.util.{SimpleCommandUtilPublisher, SimpleDataUtilPublisher}
import iot.pood.rules.actor.RulesAppGuardian

import scala.io.StdIn
/**
  * Created by rafik on 6.6.2017.
  */
object RulesApplication extends App with Log{

  log.info("Application RULES start")
  Configuration.init()
  val httpConfig = HttpConfig.httpConfig(Configuration.appConfig)
  val integrationConfig = IntegrationConfig(Configuration.appConfig)

  val system = ActorSystem()
  val mainActor = system.actorOf(RulesAppGuardian.props(integrationConfig),RulesAppGuardian.NAME)

  val simpleCommandPublisher = system.actorOf(SimpleCommandUtilPublisher.props(mainActor),SimpleCommandUtilPublisher.NAME)
  val simpleDataPublisher = system.actorOf(SimpleDataUtilPublisher.props(mainActor),SimpleDataUtilPublisher.NAME)


  log.info("Press ANY key to stop...")
  StdIn.readLine()
  system.terminate()
  log.info("Application RULES stop")

}


