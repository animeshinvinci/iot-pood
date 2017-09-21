package iot.pood.integration.actors

import java.util.Date

import akka.actor.Actor.Receive
import akka.actor.{ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import cakesolutions.kafka.KafkaConsumer
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe, Unsubscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, Extractor, KafkaConsumerActor}
import com.typesafe.config.Config
import iot.pood.base.actors.BaseActor
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
object Consumer extends IntegrationConfig{

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
  def propsData: Props = props[DataMessage](DATA,new SimpleStringDeserializer[DataMessage])

  //command
  def propsCommand: Props = props[CommandMessage](COMMAND,new SimpleStringDeserializer[CommandMessage])


  private def props[T: TypeTag](name: String, deserializer: Deserializer[T]) = {
    val consumer = component(name)
    val kafkaConsumerConf = KafkaConsumer.Conf(
      new StringDeserializer,
      deserializer,
      bootstrapServers = consumer.servers,
      groupId = consumer.groupId,
      enableAutoCommit = consumer.autoCommit,
      autoOffsetReset = OffsetResetStrategy.LATEST)
      .withConf(config)
    val actorConf = KafkaConsumerActor.Conf(1.seconds, 3.seconds)
    Props(new ConsumerActor[T](consumer,kafkaConsumerConf,actorConf))
  }

}


class ConsumerActor[T: TypeTag](consumerConfig: IntegrationConfigProperty,
                                kafkaConfig: KafkaConsumer.Conf[String, T],
                                actorConfig: KafkaConsumerActor.Conf) extends BaseActor {

  import Consumer.SubscribeMessage._

  var listeners = mutable.Set[ActorRef]()

  val consumer = context.actorOf(
    KafkaConsumerActor.props(kafkaConfig, actorConfig, self)
  )
  protected val extractor = ConsumerRecords.extractor[String, T]

  consumer ! Subscribe.AutoPartition(Set(consumerConfig.topic))

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
      log.info("Server: {}",consumerConfig.servers)
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
