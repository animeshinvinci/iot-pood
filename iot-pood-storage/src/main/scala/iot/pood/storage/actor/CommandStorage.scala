package iot.pood.storage.actor

import akka.actor.Actor.Receive
import akka.actor.Props
import iot.pood.base.actors.BaseActor
import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage

/**
  * Created by rafik on 26.9.2017.
  */
object CommandStorage {

  val NAME = "commandStorage"

  def props() = Props(new CommandStorageActor)

}

class CommandStorageActor extends BaseActor {

  override def receive = {
    case m: CommandMessage => log.info("Save command message: {}",m)
  }

}

