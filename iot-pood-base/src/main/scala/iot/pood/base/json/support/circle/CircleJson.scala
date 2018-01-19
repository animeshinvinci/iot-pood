package iot.pood.base.json.support.circle

import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.generic.AutoDerivation

trait CircleJson extends FailFastCirceSupport with AutoDerivation{

  import io.circe._

  implicit val printer: Printer = Printer(preserveOrder = true,dropNullKeys = true,indent = "")

}
