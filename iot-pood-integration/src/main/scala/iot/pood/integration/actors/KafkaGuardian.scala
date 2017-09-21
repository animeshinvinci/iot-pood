package iot.pood.integration.actors

import akka.actor.Actor.Receive
import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.integration.json.integration.JsonIntegrationMessages._

/**
  * Created by rafik on 31.7.2017.
  */
object IntegrationGuardian extends IntegrationConfig{

  val NAME = "INTEGRATION"

  sealed trait IntegrationMessage{
    def messageId: Long
  }


  object RegisterMessages {

    //register listener
    case class RegisterDataListener(messageId: Long,actorRef: ActorRef) extends IntegrationMessage

    case class RegisterCommandListener(messageId: Long, actorRef: ActorRef) extends IntegrationMessage

    case class ListenerRegistered(messageId: Long) extends IntegrationMessage

    //get producer
    case class ProducerRequest(messageId: Long,actorRef: ActorRef) extends IntegrationMessage
    case class ProducerSend(messageId: Long, actorRef: ActorRef) extends IntegrationMessage
  }


  def props(): Props = Props(new KafkaGuardianActor(config))

}

class KafkaGuardianActor(config: Config) extends BaseActor {

  import IntegrationGuardian.RegisterMessages._
  import Consumer.SubscribeMessage._

  val dataConsumer = context.actorOf(Consumer.propsData,Consumer.DATA)

  val commandConsumer = context.actorOf(Consumer.propsCommand,Consumer.COMMAND)

  val producer = context.actorOf(Producer.props(),Producer.NAME)


  override def receive: Receive = {
    case m: RegisterDataListener => {
      log.info("Register actor: {} for data listener",m.actorRef)
      dataConsumer ! SubscribeListener(m.messageId,m.actorRef)
      sender() ! ListenerRegistered(m.messageId)
    }
    case m: RegisterCommandListener => {
      log.info("Register actor: {} fro command listener",m.actorRef)
      commandConsumer ! SubscribeListener(m.messageId,m.actorRef)
      sender() ! ListenerRegistered(m.messageId)
    }
    case m: ProducerRequest =>{
      log.info("Request for producer: {}",sender)
      m.actorRef ! ProducerSend(m.messageId,producer)
    }
  }
}
