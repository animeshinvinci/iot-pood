package iot.pood.management.security

import scala.util.Try

/**
  * Created by rafik on 11.10.2017.
  */
trait TokenService[T] {

  import iot.pood.base.model.user.UserMessages._
  import iot.pood.base.model.security.SecurityMessages._

  def parse(token: String):Try[User]

  def create(uniqueUser: User): T


}
