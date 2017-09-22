package iot.pood.rules.engine

import akka.actor.Actor.Receive
import akka.actor.Props
import iot.pood.base.actors.BaseActor
import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages._

/**
  * Created by rafik on 22.9.2017.
  */
object CommandRulesEngine {

  val NAME = "commandEngine"

  def props() = Props(new CommandRulesEngineActor)

}

class CommandRulesEngineActor extends BaseActor {
  override def receive = {
    case c: CommandMessage => {
      log.info("Command: {}",c)
    }
  }
}



