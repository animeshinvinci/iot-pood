package iot.pood.management.dto

import iot.pood.base.json.api.JsonApiV1
import iot.pood.management.model.BaseModel.ObjectStatus
import iot.pood.management.model.DeviceModel.{BaseObject, Config, DataHolder}
import org.joda.time.DateTime
import reactivemongo.bson.Macros.Annotations.Key

object DeviceDto {

    case class CreateDeviceWithGroup(groupName: Option[String], groupComment: Option[String], name: Option[String], comment: Option[String])
    case class CreateDevice(name: Option[String],comment: Option[String])

    case class UpdateDevice(version: Option[String],name: Option[String],comment: Option[String],status: Option[ObjectStatus])

}

trait DeviceJson extends JsonApiV1
{
    import DeviceDto._
}

