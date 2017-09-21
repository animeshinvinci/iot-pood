package iot.pood.integration.consumer

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import cakesolutions.kafka.akka.KafkaConsumerActor.{Confirm, Subscribe}
import cakesolutions.kafka.akka.{ConsumerRecords, KafkaConsumerActor}
import cakesolutions.kafka.{KafkaConsumer, KafkaProducer}
import com.typesafe.config.Config
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.duration.DurationInt

import scala.collection.mutable

/**
  * Companion object for creating producer actor
  *
  *
  * Created by rafik on 11.6.2017.
  */
object DataConsumer {

//  private val GROUP_PROPERTY = "group"
//
//  def apply(config: Config): Props = {
//    val consumerConf = KafkaConsumer.Conf(
//        new StringDeserializer,
//        new DataRecordDeserializer,
//      groupId = config.getConfig("kafka").getString(GROUP_PROPERTY),
//      enableAutoCommit = false,
//      autoOffsetReset = OffsetResetStrategy.LATEST)
//        .withConf(config)
//    val actorConf = KafkaConsumerActor.Conf(300.millis,1.seconds)
//    Props(new DataConsumerActor(consumerConf,actorConf,config))
//  }
//
//  /**
//    * General listener command
//    */
//  sealed trait  Listener{
//
//    val listener: ActorRef
//
//  }
//
//  /**
//    * Command for register actor which want be informed about data receive from consumer
//    *
//    * @param listener
//    */
//  case class RegisterListener(listener: ActorRef) extends Listener
//
//  /**
//    * Command for unregister actor which doesn't want to be informed about data receive from consumer anymore
//    *
//    * @param listener
//    */
//  case class UnregisterListener(listener: ActorRef) extends Listener
//
//
//  /**
//    * Message for inform listener
//    *
//    * @param data
//    */
//  case class DataMessage(data: Set[DataRecord])
//
//}
//
///**
//  * Data consumer actor. Receive DataRecord from kafka stream
//  *
//  * @param kafkaConfig
//  * @param actorConfig
//  * @param config
//  */
//class DataConsumerActor(
//                         kafkaConfig: KafkaConsumer.Conf[String, DataRecord],
//                         actorConfig: KafkaConsumerActor.Conf,
//                         config: Config) extends Actor with ActorLogging{
//
//  val dataListener = mutable.HashSet[ActorRef]()
//
//  /**
//    * Data extractor for DataRecord
//    */
//  val dataRecordExtractor = ConsumerRecords.extractor[String,DataRecord]
//
//  /**
//    * Kafka consumer actor
//    *
//    */
//  val consumer = context.actorOf(KafkaConsumerActor.props(kafkaConfig,actorConfig,self))
//
//
//
//  consumer ! Subscribe.AutoPartition(List("data.v1"))
//  /**
//    * Supervisor strategy for kafka consumer
//    *
//    * @return
//    */
//  override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10) {
//    case _: KafkaConsumerActor.ConsumerException =>
//      log.info("Consumer exception caught. Restarting consumer.")
//      SupervisorStrategy.Restart
//    case _ =>
//      SupervisorStrategy.Escalate
//  }
//
//  override def receive: Receive = {
//
//    case dataRecordExtractor(data) =>{
//      val dataRecords = transformData(data.pairs)
//      informListener(dataListener,dataRecords)
//      sender() ! Confirm(data.offsets,commit = true)
//      if(log.isDebugEnabled) {
//        dataRecords.foreach(r => {
//          log.debug("Data: "+r)
//        })
//      }
//    }
//    case RegisterListener(actor) =>{
//      log.info("Register actor: {} as listener",actor)
//      dataListener +=actor
//    }
//    case UnregisterListener(actor) => {
//      log.info("Unregister actor: {} as listener",actor)
//      dataListener -=actor
//    }
//    case _ => log.error("Unable to map message: ")
//  }
//
//
//  private def transformData(data: Seq[(Option[String], DataRecord)]) = data.map(record => record._2)
//
//
//  private def informListener(listeners: mutable.Set[ActorRef],data: Iterable[DataRecord]) = {
//    listeners.foreach(actor =>{
//      actor ! DataMessage(data.toSet)
//    })
//  }
}



