package iot.pood.storage.actor

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.base.integration.IntegrationConfig.IntegrationConfig
import iot.pood.base.messages.integration.ComponentMessages.{GetIntegrationComponentRequest, GetIntegrationComponentResponse}
import iot.pood.integration.actors.IntegrationGuardian
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ListenerRegistered, RegisterCommandListener, RegisterDataListener}

/**
  * Created by rafik on 9.8.2017.
  */
object StorageAppGuardian {

  val NAME = "storageGuardian"


  def props(integrationConfig: IntegrationConfig) =Props(new RulesAppGuardianActor(integrationConfig))

}

case class RulesAppGuardianActor(integrationConfig: IntegrationConfig) extends BaseActor {

  val integrationActor = context.actorOf(IntegrationGuardian.props(integrationConfig),IntegrationGuardian.NAME)

  val commandStorageActor = context.actorOf(CommandStorage.props(),CommandStorage.NAME)

  val dataStorageActor = context.actorOf(DataStorage.props(),DataStorage.NAME)

  def receive = {

    case ListenerRegistered => log.debug("Listener registered")
    case message: GetIntegrationComponentRequest => message.sender ! GetIntegrationComponentResponse(integrationActor)
  }

  @scala.throws[Exception](classOf[Exception])
  override def preStart() = {
    super.preStart()
    integrationActor ! RegisterDataListener(1l,dataStorageActor)
    integrationActor ! RegisterCommandListener(1l,commandStorageActor)
  }
}

