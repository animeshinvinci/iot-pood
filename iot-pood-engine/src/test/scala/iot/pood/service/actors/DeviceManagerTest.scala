package iot.pood.service.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import cakesolutions.kafka.akka.KafkaProducerActor.Matcher
import iot.pood.base.actors.BaseActorTest
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * Created by rafik on 13.7.2017.
  */
class DeviceManagerTest extends BaseActorTest{


  "Echo server " must {
    "Send back message without change" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello"
      expectMsg("hello")
    }
  }
}
