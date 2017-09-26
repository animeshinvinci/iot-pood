package iot.pood.storage.actor

import akka.actor.Actor.Receive
import akka.actor.Props
import iot.pood.base.actors.BaseActor
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages.DataMessage

/**
  * Created by rafik on 26.9.2017.
  */
object DataStorage {

  val NAME = "dataStorage"

  def props() = Props(new DataStorageActor)

}


class DataStorageActor extends BaseActor {
  override def receive = {
    case m: DataMessage => log.info("Store data message: {}",m)
  }
}

