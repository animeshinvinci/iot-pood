package iot.pood.management.actor

import akka.util.Timeout
import iot.pood.base.actors.BaseActorTest
import iot.pood.management.base.TestSecurityConfig
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration.DurationDouble

/**
  * Created by rafik on 12.10.2017.
  */
abstract class SecurityTests extends BaseActorTest with TestSecurityConfig
  with Matchers {
  import org.scalatest._

  implicit val timeout = Timeout(5 seconds)
}
