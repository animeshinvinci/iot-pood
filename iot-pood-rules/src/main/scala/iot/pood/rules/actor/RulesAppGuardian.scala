package iot.pood.rules.actor

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.base.integration.IntegrationConfig.IntegrationConfig
import iot.pood.base.messages.integration.ComponentMessages.{GetIntegrationComponentRequest, GetIntegrationComponentResponse}
import iot.pood.integration.actors.IntegrationGuardian
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ListenerRegistered, RegisterCommandListener, RegisterDataListener}
import iot.pood.rules.engine.{CommandRulesEngine, DataRulesEngine}

/**
  * Created by rafik on 22.9.2017.
  */
object RulesAppGuardian {

  val NAME = "rulesGuardian"


  def props(integrationConfig: IntegrationConfig) =Props(new RulesAppGuardianActor(integrationConfig))

}

case class RulesAppGuardianActor(integrationConfig: IntegrationConfig) extends BaseActor {

  val integrationActor = context.actorOf(IntegrationGuardian.props(integrationConfig),IntegrationGuardian.NAME)

  val commandEngineActor = context.actorOf(CommandRulesEngine.props(),CommandRulesEngine.NAME)

  val dataEngineActor = context.actorOf(DataRulesEngine.props(),DataRulesEngine.NAME)

  def receive = {

    case ListenerRegistered => log.debug("Listener registered")
    case message: GetIntegrationComponentRequest => message.sender ! GetIntegrationComponentResponse(integrationActor)
}

  @scala.throws[Exception](classOf[Exception])
  override def preStart() = {
    super.preStart()
    integrationActor ! RegisterDataListener(1l,dataEngineActor)
    integrationActor ! RegisterCommandListener(1l,commandEngineActor)
  }
}



