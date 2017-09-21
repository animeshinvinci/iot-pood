package iot.pood.service.actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import DeviceManager._

/**
  * Device manager. Handle registering device group and device
  *
  * Created by rafik on 13.7.2017.
  */
object DeviceManager {

  def props():Props = Props(new DeviceManagerActor)

  /**
    * Ask for registering device in specific group
    *
    * @param groupId
    * @param deviceId
    */
  final case class RequestRegisterDevice(groupId: String, deviceId: String)

  /**
    * Registered device OK
    *
    */
  case object DeviceRegistered

}

class DeviceManagerActor extends Actor with ActorLogging{

  var groupIdToActor = Map.empty[String,ActorRef]
  var actorToGroupId = Map.empty[ActorRef,String]

  override def receive = {
    case message @ RequestRegisterDevice(groupId,_) => {
      groupIdToActor.get(groupId) match {
        case Some(ref) => {
          log.info("Message is forwarded to actor: {}",ref)
          ref forward message
        }
        case None => {
          val groupActor = context.actorOf(DeviceGroup.props(groupId))
          groupIdToActor += groupId -> groupActor
          actorToGroupId += groupActor -> groupId
        }
      }
    }

    case Terminated(groupActor) => {
      actorToGroupId(groupActor)
    }

  }

  override def preStart() = {
    log.debug("Pre start")
  }

  override def postStop() = {
    log.debug("Post stop")
  }

}
