package iot.pood.integration.actors

import akka.actor.Actor.Receive
import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import iot.pood.base.actors.BaseActor
import iot.pood.base.integration.IntegrationConfig.IntegrationConfig

/**
  * Created by rafik on 31.7.2017.
  */
object IntegrationGuardian {

  val NAME = "integration"

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


  def props(integrationConfig: IntegrationConfig): Props = Props(new KafkaGuardianActor(integrationConfig))

}

class KafkaGuardianActor(integrationConfig: IntegrationConfig) extends BaseActor {

  import IntegrationGuardian.RegisterMessages._
  import Consumer.SubscribeMessage._

  val dataConsumer = context.actorOf(Consumer.propsData(integrationConfig),Consumer.DATA)

  val commandConsumer = context.actorOf(Consumer.propsCommand(integrationConfig),Consumer.COMMAND)

  val producer = context.actorOf(Producer.props(integrationConfig),Producer.NAME)


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
