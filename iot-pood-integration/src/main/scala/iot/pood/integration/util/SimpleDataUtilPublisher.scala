package iot.pood.integration.util

import java.util.Calendar

import akka.actor.{ActorRef, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.base.messages.integration.ComponentMessages.{GetIntegrationComponentRequest, GetIntegrationComponentResponse}
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages.{DataDeviceStateMessage, DataMessage}
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ProducerRequest, ProducerSend}
import iot.pood.integration.actors.Producer.PublishProtocol.PublishData

import scala.concurrent.duration.DurationInt

/**
  * Created by rafik on 22.9.2017.
  */
object SimpleDataUtilPublisher {

  val NAME = "simpleDataPublisher"

  def props(actorRef: ActorRef) = Props(new SimpleDataUtilPublisherActor(actorRef))

}

class SimpleDataUtilPublisherActor(mainActor: ActorRef) extends BaseActor{

  import context._

  mainActor ! GetIntegrationComponentRequest(self)

  var producer: Option[ActorRef] = None

  override def receive = producerAsk

  def producerAsk: Receive = {
    case ProducerSend(_,actorRef) => {
      producer = Some(actorRef)
      context.become(startProduce)
      system.scheduler.scheduleOnce(500 millis, self, "tick")
    }
    case GetIntegrationComponentResponse(actorRef) => actorRef ! ProducerRequest(1l,self)
  }
  def startProduce: Receive = {
    case "tick" => {
      log.info("Tick. Send Data message....")
      producer.get ! PublishData(123l,self,DataMessage(111l,Calendar.getInstance().getTime,"1",None,Some(DataDeviceStateMessage("testing"))))
      system.scheduler.scheduleOnce(1000 millis, self, "tick")
    }
  }
}

