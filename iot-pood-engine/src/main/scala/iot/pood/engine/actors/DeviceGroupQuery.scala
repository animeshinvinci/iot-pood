package iot.pood.engine.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}

import scala.concurrent.duration.FiniteDuration

/**
  * Created by rafik on 23.7.2017.
  */
object DeviceGroupQuery {

  def props(actorToDeviceId: Map[ActorRef,String], requestId:Long, requester:ActorRef, timeout: FiniteDuration):Props = {
    Props(new DeviceGroupQueryActor(actorToDeviceId,requestId,requester,timeout))
  }

  case object CollectionTimeout

}

class DeviceGroupQueryActor(actorToDeviceId: Map[ActorRef, String],
                            requestId:       Long,
                            requester:       ActorRef,
                            timeout:         FiniteDuration) extends Actor with ActorLogging {

  import DeviceGroupQuery._
  import context.dispatcher
  val queryTimeoutTimer = context.system.scheduler.scheduleOnce(timeout, self, CollectionTimeout)

  override def preStart(): Unit = {
    actorToDeviceId.keysIterator.foreach { deviceActor =>
      context.watch(deviceActor)
      deviceActor ! Device.ReadData(0)
    }
  }

  override def postStop(): Unit = {
    queryTimeoutTimer.cancel()
  }

  override def receive: Receive =
    waitingForReplies(
      Map.empty,
      actorToDeviceId.keySet
    )

  def waitingForReplies(
                         repliesSoFar: Map[String, DeviceGroup.DataReading],
                         stillWaiting: Set[ActorRef]
                       ): Receive = {
    case Device.ReadDataResponse(0, valueOption) =>
      val deviceActor = sender()
      val reading = valueOption match {
        case Some(value) => DeviceGroup.DeviceData(Some(value))
        case None        => DeviceGroup.DataNotAvailable
      }
      receivedResponse(deviceActor, reading, stillWaiting, repliesSoFar)

    case Terminated(deviceActor) =>
      receivedResponse(deviceActor, DeviceGroup.DeviceNotAvailable, stillWaiting, repliesSoFar)

    case CollectionTimeout =>
      val timedOutReplies =
        stillWaiting.map { deviceActor =>
          val deviceId = actorToDeviceId(deviceActor)
          deviceId -> DeviceGroup.DeviceTimedOut
        }
      requester ! DeviceGroup.RespondAllData(requestId, repliesSoFar ++ timedOutReplies)
      context.stop(self)
  }

  def receivedResponse(
                        deviceActor:  ActorRef,
                        reading:      DeviceGroup.DataReading,
                        stillWaiting: Set[ActorRef],
                        repliesSoFar: Map[String, DeviceGroup.DataReading]
                      ): Unit = {
    context.unwatch(deviceActor)
    val deviceId = actorToDeviceId(deviceActor)
    val newStillWaiting = stillWaiting - deviceActor

    val newRepliesSoFar = repliesSoFar + (deviceId -> reading)
    if (newStillWaiting.isEmpty) {
      requester ! DeviceGroup.RespondAllData(requestId, newRepliesSoFar)
      context.stop(self)
    } else {
      context.become(waitingForReplies(newRepliesSoFar, newStillWaiting))
    }
  }

}
