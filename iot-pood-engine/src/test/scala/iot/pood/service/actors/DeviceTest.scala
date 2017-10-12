package iot.pood.service.actors

import akka.testkit.TestProbe
import iot.pood.base.actors.BaseActorTest
import iot.pood.engine.actors.Device
import iot.pood.engine.actors.Device.{DataRecorded, ReadData, ReadDataResponse, RecordData}
import iot.pood.engine.actors.DeviceManager.{DeviceRegistered, RequestRegisterDevice}

/**
  * Created by rafik on 14.7.2017.
  */
class DeviceTest extends BaseActorTest{

  "Device actor " must {
    "response on registration message" in {
      val deviceActor = system.actorOf(Device.props("group1","device123"))
      deviceActor ! RequestRegisterDevice("group1","device123")
      expectMsg(DeviceRegistered)
    }

    "record data send to device" in {
      val probe = TestProbe()

      val deviceActor = system.actorOf(Device.props("group1","device123"))

      deviceActor.tell(RecordData(123l,"100"),probe.ref)
      probe.expectMsg(DataRecorded(requestId = 123l))
    }

    "send empty data when data wasn't recorded before" in {
      val probe = TestProbe()

      val deviceActor = system.actorOf(Device.props("group1","device123"))

      deviceActor.tell(ReadData(123l),probe.ref)
      probe.expectMsg(ReadDataResponse(requestId = 123l,data = None))
    }

    "send recorded value 100 when value was collected before" in {
      val probe = TestProbe()

      val deviceActor = system.actorOf(Device.props("group1","device123"))

      deviceActor.tell(RecordData(123l,"100"),probe.ref)
      probe.expectMsg(DataRecorded(requestId = 123l))

      deviceActor.tell(ReadData(1234l),probe.ref)
      probe.expectMsg(ReadDataResponse(requestId = 1234l,data = Some("100")))
    }
  }

}
