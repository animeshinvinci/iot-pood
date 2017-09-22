package iot.pood.integration.actors

import java.util.Date

import akka.actor.{ActorRef, Props}
import cakesolutions.kafka.akka.{KafkaProducerActor, ProducerRecords}
import cakesolutions.kafka.{KafkaProducer, KafkaProducerRecord}
import com.typesafe.config.Config
import iot.pood.base.actors.BaseActor
import iot.pood.base.integration.IntegrationConfig.{IntegrationConfig, IntegrationProperty}
import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage
import org.apache.kafka.common.serialization.{Serializer, StringSerializer}
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages._
import iot.pood.integration.json.integration.JsonIntegrationMessages._
import iot.pood.integration.json.integration.SimpleStringSerializer

/**
  * Created by rafik on 31.7.2017.
  */
object Producer extends IntegrationComponent{

  val NAME = "producer"

  sealed trait Message{

    def messageId: Long

  }

  sealed trait PublishMessage extends Message
  {
    def actorRef: ActorRef
  }


  object PublishProtocol
  {
    case class PublishData(messageId: Long, actorRef: ActorRef,data: DataMessage) extends PublishMessage

    case class PublishCommand(messageId: Long, actorRef: ActorRef,command: CommandMessage) extends PublishMessage

    case class PublishOk(messageId: Long) extends Message

    case class PublishError(messageId: Long) extends Message
  }

  object KafkaProtocol {

    case class DataOk(messageId: Long, actorRef: ActorRef) extends PublishMessage

    case class DataError(messageId: Long, actorRef: ActorRef) extends PublishMessage
  }


  def props(integrationConfig: IntegrationConfig): Props = {
    val dataConfig = integrationConfig.get(DATA)
    val commandConfig = integrationConfig.get(COMMAND)
    Props(new ProducerActor(integrationConfig.componentConfig,dataConfig,commandConfig))
  }


}

 class ProducerActor(config: Config, dataConfig: IntegrationProperty,
                     commandConfig: IntegrationProperty) extends BaseActor{

    import Producer.KafkaProtocol._
    import Producer.PublishProtocol._

   val dataProducerActor = context.actorOf(KafkaProducerActor.props( KafkaProducer.Conf(
         config,
         keySerializer = new StringSerializer,
         new SimpleStringSerializer[DataMessage])))


   val commandProducerActor = context.actorOf(KafkaProducerActor.props( KafkaProducer.Conf(
         config,
         keySerializer = new StringSerializer,
         new SimpleStringSerializer[CommandMessage])))


  override def receive: Receive = {
    case m:PublishData => {
        dataProducerActor ! ProducerRecords.fromValuesWithKey(dataConfig.topic,Some(""),Seq(m.data),
          Some(DataOk(m.messageId,m.actorRef)),Some(DataError(m.messageId,m.actorRef)))
      }
    case m:PublishCommand =>{
        commandProducerActor ! ProducerRecords.fromValuesWithKey(commandConfig.topic,Some(""),Seq(m.command),
          Some(DataOk(m.messageId,m.actorRef)),Some(DataError(m.messageId,m.actorRef)))
    }
    case m:DataOk => {
        m.actorRef ! PublishOk(m.messageId)
    }
    case m:DataError => {
        m.actorRef ! PublishError(m.messageId)
    }
  }

  override def postStop() = {
    super.postStop()
    context.stop(dataProducerActor)
    context.stop(commandProducerActor)
  }
}



