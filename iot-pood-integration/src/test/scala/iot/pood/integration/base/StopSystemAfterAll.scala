package iot.pood.integration.base

import akka.testkit.TestKit
import org.scalatest.{BeforeAndAfterAll, Suite}

/**
  * Created by rafik on 22.5.2017.
  */
trait StopSystemAfterAll extends BeforeAndAfterAll{

  this: TestKit with Suite =>
  override protected def afterAll() = {
    super.afterAll()
    system.terminate()
  }
}
