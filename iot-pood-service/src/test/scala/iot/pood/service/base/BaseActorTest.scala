package iot.pood.service.base

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit, TestProbe}
import cakesolutions.kafka.akka.KafkaProducerActor.Matcher
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

/**
  * Created by rafik on 14.7.2017.
  */
abstract class BaseActorTest extends TestKit(ActorSystem("iot-pood-service")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {


  override protected def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }
}
