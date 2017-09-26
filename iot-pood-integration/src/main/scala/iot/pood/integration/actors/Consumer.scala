package iot.pood.integration.actors

import java.util.Date

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe, Unsubscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, Extractor, KafkaConsumerActor}
import com.typesafe.config.Config
import iot.pood.base.actors.BaseActor
import iot.pood.base.integration.IntegrationConfig.{IntegrationConfig, IntegrationProperty}
import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.{Deserializer, Serializer, StringDeserializer}
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages._
import iot.pood.integration.json.integration.SimpleStringDeserializer

import scala.collection.mutable
import scala.concurrent.duration.DurationDouble
import scala.reflect.runtime.universe._

/**
  * Created by rafik on 31.7.2017.
  */
object Consumer{

  val DATA = "data";
  val COMMAND = "command";

  import iot.pood.integration.json.integration.JsonIntegrationMessages._

  // messages
  sealed trait ConsumerMessage {
    def  messageId: Long
  }

  object SubscribeMessage {

    case class SubscribeListener(messageId: Long, actorRef: ActorRef) extends ConsumerMessage

    case class ListenerSubscribed(messageId: Long) extends ConsumerMessage

    case class UnSubscribeListener(messageId: Long, actorRef: ActorRef) extends ConsumerMessage

    case class ListenerUnSubscribed(messageId: Long) extends ConsumerMessage

  }
  //data
  def propsData(integrationConfig: IntegrationConfig): Props = props[DataMessage](integrationConfig,
                                                              DATA,new SimpleStringDeserializer[DataMessage])

  //command
  def propsCommand(integrationConfig: IntegrationConfig): Props = props[CommandMessage](integrationConfig,
                                                              COMMAND,new SimpleStringDeserializer[CommandMessage])


  private def props[T: TypeTag](integrationConfig: IntegrationConfig,
                                name: String, deserializer: Deserializer[T]) = {
    val consumer = integrationConfig.get(name)
    val kafkaConsumerConf = KafkaConsumer.Conf(
      new StringDeserializer,
      deserializer,
      bootstrapServers = consumer.servers,
      groupId = consumer.groupId match {
        case None => null
        case Some(v) => v
      },
      enableAutoCommit = consumer.autoCommit,
      autoOffsetReset = OffsetResetStrategy.LATEST)
      .withConf(integrationConfig.appConfig)
    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)
    Props(new ConsumerActor[T](consumer,kafkaConsumerConf,actorConf))
  }

}


class ConsumerActor[T: TypeTag](configuration: IntegrationProperty,
                                kafkaConfig: KafkaConsumer.Conf[String, T],
                                actorConfig: KafkaConsumerActor.Conf) extends BaseActor {

  import Consumer.SubscribeMessage._

  var listeners = mutable.Set[ActorRef]()

  val consumer = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )
  protected val extractor = ConsumerRecords.extractor[String, T]

  consumer ! Subscribe.AutoPartition(Set(configuration.topic))

  override def receive = {

    case m: SubscribeListener => {
      log.info("Subscribe actor: {}",m.actorRef)
      listeners += m.actorRef
      sender() ! ListenerSubscribed(m.messageId)
    }
    case m: UnSubscribeListener => {
      log.info("UnSubscribe actor: {}",m.actorRef)
      listeners.remove(m.actorRef)
      sender()! ListenerUnSubscribed(m.messageId)
    }
    case extractor(data) =>{
      data.pairs.foreach(p =>{
        log.info("Pair 1: {} 2: {}",p._1,p._2)
      })
      data.records.forEach(r =>{
        listeners.foreach(l => {
          log.info("Publish for actor: {} value: {}",l,r.value())
          l ! r.value()
        })
        sender() ! Confirm(data.offsets, commit = true)
      })
    }
  }

  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 50) {
    case _: KafkaConsumerActor.ConsumerException =>
      log.info("Consumer exception caught. Restarting consumer.")
      SupervisorStrategy.Restart
    case _ =>
      SupervisorStrategy.Escalate
  }


}
