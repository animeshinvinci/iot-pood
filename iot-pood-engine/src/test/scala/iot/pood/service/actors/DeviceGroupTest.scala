package iot.pood.service.actors

import akka.actor.PoisonPill
import akka.testkit.TestProbe
import iot.pood.base.actors.BaseActorTest
import iot.pood.engine.actors.{Device, DeviceGroup, DeviceManager}

/**
  * Created by rafik on 20.7.2017.
  */
class DeviceGroupTest extends BaseActorTest {


  "Device group actor " must {

    "be able to list active device " in {

      val probe = TestProbe()
      val groupActor = system.actorOf(DeviceGroup.props("group"))
      groupActor.tell(DeviceManager.RequestRegisterDevice("group","device1"),probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)

      groupActor.tell(DeviceManager.RequestRegisterDevice("group","device2"),probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)

      groupActor.tell(DeviceGroup.RequestDeviceList(requestId = 1l),probe.ref)
      probe.expectMsg(DeviceGroup.ReplyDeviceList(requestId = 1l,Set("device1","device2")))

    }

    "return same actor for same deviceId" in {

      val probe = TestProbe()
      val groupActor = system.actorOf(DeviceGroup.props("group"))

      groupActor.tell(DeviceManager.RequestRegisterDevice("group","device1"),probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)
      val deviceActor1 = probe.lastSender


      groupActor.tell(DeviceManager.RequestRegisterDevice("group","device1"),probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)
      val deviceActor2 = probe.lastSender

      deviceActor1 should === (deviceActor2)

    }

    "be able to list active devices after one shuts down" in {
      val probe = TestProbe()
      val groupActor = system.actorOf(DeviceGroup.props("group"))

      groupActor.tell(DeviceManager.RequestRegisterDevice("group", "device1"), probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)
      val toShutDown = probe.lastSender

      groupActor.tell(DeviceManager.RequestRegisterDevice("group", "device2"), probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)

      groupActor.tell(DeviceGroup.RequestDeviceList(requestId = 0), probe.ref)
      probe.expectMsg(DeviceGroup.ReplyDeviceList(requestId = 0, Set("device1", "device2")))

      probe.watch(toShutDown)
      toShutDown ! PoisonPill
      probe.expectTerminated(toShutDown)

      // using awaitAssert to retry because it might take longer for the groupActor
      // to see the Terminated, that order is undefined
//      probe.awaitAssert {
//        groupActor.tell(DeviceGroup.RequestDeviceList(requestId = 1), probe.ref)
//        probe.expectMsg(DeviceGroup.ReplyDeviceList(requestId = 1, Set("device2")))
//      }
    }

    "be able to collect temperatures from all active devices" in {
      val probe = TestProbe()
      val groupActor = system.actorOf(DeviceGroup.props("group"))

      groupActor.tell(DeviceManager.RequestRegisterDevice("group", "device1"), probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)
      val deviceActor1 = probe.lastSender

      groupActor.tell(DeviceManager.RequestRegisterDevice("group", "device2"), probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)
      val deviceActor2 = probe.lastSender

      groupActor.tell(DeviceManager.RequestRegisterDevice("group", "device3"), probe.ref)
      probe.expectMsg(DeviceManager.DeviceRegistered)
      val deviceActor3 = probe.lastSender

      // Check that the device actors are working
      deviceActor1.tell(Device.RecordData(requestId = 0, "1.0"), probe.ref)
      probe.expectMsg(Device.DataRecorded(requestId = 0))
      deviceActor2.tell(Device.RecordData(requestId = 1, "2.0"), probe.ref)
      probe.expectMsg(Device.DataRecorded(requestId = 1))
      // No temperature for device3

      groupActor.tell(DeviceGroup.RequestAllData(requestId = 0), probe.ref)
      probe.expectMsg(
        DeviceGroup.RespondAllData(
          requestId = 0,
          data = Map(
            "device1" -> DeviceGroup.DeviceData(Some("1.0")),
            "device2" -> DeviceGroup.DeviceData(Some("2.0")),
            "device3" -> DeviceGroup.DataNotAvailable)))
    }

  }
}