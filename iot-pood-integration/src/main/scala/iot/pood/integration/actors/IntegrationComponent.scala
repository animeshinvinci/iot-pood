package iot.pood.integration.actors

/**
  * Created by rafik on 8.9.2017.
  */
trait IntegrationComponent extends IntegrationConfig{

  val DATA = "data";
  val COMMAND = "command";

}

trait IntegrationMessage {
  def messageId: Long
}

