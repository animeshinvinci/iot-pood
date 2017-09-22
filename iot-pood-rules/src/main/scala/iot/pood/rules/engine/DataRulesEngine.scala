package iot.pood.rules.engine

import akka.actor.Actor.Receive
import akka.actor.Props
import iot.pood.base.actors.BaseActor
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages.DataMessage

/**
  * Created by rafik on 22.9.2017.
  */
object DataRulesEngine {

  val NAME = "dataEngine"

  def props() = Props(new DataRulesEngineActor)

}

class DataRulesEngineActor extends BaseActor {

  def receive = {
    case message: DataMessage => {
      log.info("Data message: {}",message)
    }
  }
}

