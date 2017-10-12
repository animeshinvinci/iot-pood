package iot.pood.base.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
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
