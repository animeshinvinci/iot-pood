package iot.pood.management.base

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by rafik on 12.10.2017.
  */
abstract class BaseTest extends FlatSpec with Matchers {
  import org.scalatest._
  import Matchers._
  import scala.concurrent.duration._
}
