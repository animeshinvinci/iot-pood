package iot.pood.integration.util

import java.util.Calendar

import akka.actor.{ActorRef, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.base.messages.integration.ComponentMessages.{GetIntegrationComponentRequest, GetIntegrationComponentResponse}
import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages.{DataDeviceStateMessage, DataMessage}
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ProducerRequest, ProducerSend}
import iot.pood.integration.actors.Producer.PublishProtocol.{PublishCommand, PublishData}

import scala.concurrent.duration.DurationInt

/**
  * Created by rafik on 22.9.2017.
  */
object SimpleCommandUtilPublisher {

  val NAME = "simpleCommandPublisher"

  def props(actorRef: ActorRef) = Props(new SimpleCommandUtilPublisherActor(actorRef))

}

class SimpleCommandUtilPublisherActor(mainActor: ActorRef) extends BaseActor{

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
      log.info("Tick. Send Command....")
      producer.get ! PublishCommand(123l,self,CommandMessage(123l,Calendar.getInstance.getTime,"set",Some("123")))
      system.scheduler.scheduleOnce(1000 millis, self, "tick")
    }
  }
}

