package iot.pood.rules

import java.time.Duration
import java.util.Calendar

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.dispatch.MonitorableThreadFactory
import com.typesafe.config.ConfigFactory
import iot.pood.base.actors.BaseActor
import iot.pood.base.config.Configuration
import iot.pood.base.http.HttpConfig
import iot.pood.base.integration.IntegrationConfig
import iot.pood.base.log.Log
import iot.pood.base.messages.integration.IntegrationMessages.CommandMessages.CommandMessage
import iot.pood.base.messages.integration.IntegrationMessages.DataMessages._
import iot.pood.base.utils.RequestUniqueCreator
import iot.pood.integration.actors.IntegrationGuardian
import iot.pood.integration.actors.IntegrationGuardian.RegisterMessages.{ProducerRequest, ProducerSend, RegisterDataListener}
import iot.pood.integration.actors.Producer.PublishProtocol.PublishData

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong
import scala.io.StdIn
/**
  * Created by rafik on 6.6.2017.
  */
object RulesApplication extends App with Log{

  log.info("Application RULES start")
  Configuration.init()
  val httpConfig = HttpConfig.httpConfig(Configuration.appConfig)
  val integrationConfig = IntegrationConfig(Configuration.appConfig)
  val system = ActorSystem()
  val guardian = system.actorOf(IntegrationGuardian.props(integrationConfig))

  val subscribe = system.actorOf(Subscribe.props(guardian),Subscribe.NAME)
  val listener = system.actorOf(Listener.props(guardian),"listener")
  log.info("Press ANY key to stop...")
  StdIn.readLine()
  system.terminate()
  log.info("Application RULES stop")


}


object Listener {

  val NAME  = "listener"

  def props(guardian: ActorRef) = {
      Props(new ListenerActor(guardian))
  }
}
class ListenerActor(guardian: ActorRef) extends BaseActor{

  import context._

  guardian ! ProducerRequest(123l,self)

  var producer: Option[ActorRef] = None

  override def receive = producerAsk

  def producerAsk: Receive = {
    case ProducerSend(_,actorRef) => {
      log.info("GET producer for listener: {}",actorRef)
      producer = Some(actorRef)
      context.become(startProduce)
      system.scheduler.scheduleOnce(500 millis, self, "tick")
    }
  }
  def startProduce: Receive = {
    case "tick" => {
      log.info("Tick. Send message....")
      producer.get ! PublishData(123l,self,DataMessage(111l,Calendar.getInstance().getTime,"1",None,Some(DataDeviceStateMessage("testing"))))
      system.scheduler.scheduleOnce(1000 millis, self, "tick")
    }
  }
}

object Subscribe {

  val NAME = "subscribe"

  def props(guardian: ActorRef) = Props(new SubscribeActor(guardian))

}

class SubscribeActor(guardian: ActorRef) extends BaseActor{

  guardian ! RegisterDataListener(1l,self)

  override def receive: Receive = {

    case data: DataMessage => {
      log.info("Data receive OK: {}",data)
    }
    case command: CommandMessage => {
      log.info("Command receive OK: {}",command)
    }
  }
}


