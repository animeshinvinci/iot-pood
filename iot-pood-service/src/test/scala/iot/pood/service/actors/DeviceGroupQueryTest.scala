package iot.pood.service.actors

import akka.testkit.TestProbe
import iot.pood.service.base.BaseActorTest

import scala.concurrent.duration.DurationLong

/**
  * Created by rafik on 23.7.2017.
  */
class DeviceGroupQueryTest extends BaseActorTest{

  "Device query actor" must {

    "return temperature value for working devices" in {
      val requester = TestProbe()

      val device1 = TestProbe()
      val device2 = TestProbe()

      val queryActor = system.actorOf(DeviceGroupQuery.props(
        actorToDeviceId = Map(device1.ref -> "device1", device2.ref -> "device2"),
        requestId = 1,
        requester = requester.ref,
        timeout = 3.seconds
      ))

      device1.expectMsg(Device.ReadData(requestId = 0))
      device2.expectMsg(Device.ReadData(requestId = 0))

      queryActor.tell(Device.ReadDataResponse(requestId = 0, Some("1.0")), device1.ref)
      queryActor.tell(Device.ReadDataResponse(requestId = 0, Some("2.0")), device2.ref)

      requester.expectMsg(DeviceGroup.RespondAllData(
        requestId = 1,
        data = Map(
          "device1" -> DeviceGroup.DeviceData(Some("1.0")),
          "device2" -> DeviceGroup.DeviceData(Some("2.0"))
        )
      ))
    }
  }

}
