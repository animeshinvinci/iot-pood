package iot.pood.base.messages.integration

import java.util.Date

/**
  * Main integration messages used in integration platform
  *
  * Created by rafik on 23.8.2017.
  */
object IntegrationMessages {

  /**
    * Common message
    *
    */
  sealed trait Message {

    /**
      * Time series ID
      * @return
      */
    def id: Long

    /**
      * Time when data was created
      * @return
      */
    def created: Date

  }

  /**
    * Message regarding device
    *
    */
  trait DeviceMessage extends Message {

    def deviceId: String

  }

  /**
    * Messages hold data
    *
    */
  object DataMessages {

    case class DataDeviceMessage(dataId: String, data: String)

    case class DataDeviceStateMessage(state: String )

    case class DataMessage(id: Long, created: Date, deviceId: String,
                           deviceData: Option[DataDeviceMessage] = None,
                           deviceState: Option[DataDeviceStateMessage] = None) extends DeviceMessage
  }


  trait Command extends Message
  {
    def command: String
  }

  /**
    * Messages hold commands
    *
    */
  object CommandMessages {

    case class CommandMessage(id: Long, created: Date, command: String,
                       groupId: Option[String] = None, deviceId: Option[String] = None) extends Command

  }

}
