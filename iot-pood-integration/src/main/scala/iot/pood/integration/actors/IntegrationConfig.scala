package iot.pood.integration.actors

import com.typesafe.config.{Config, ConfigFactory}
import iot.pood.base.config.AppConfig

import scala.collection.JavaConverters._
import iot.pood.base.exception.Exceptions._
/**
  * Created by rafik on 24.8.2017.
  */
trait IntegrationConfig extends AppConfig{

  lazy val integration = config.getConfig(IntegrationConstant.MAIN_CONFIG)
  lazy val servers = integration.getString(IntegrationConstant.SERVERS)
  lazy val components = integration.getStringList(IntegrationConstant.COMPONENTS).asScala.toList

  def component(name: String): IntegrationConfigProperty = {
    if(!(components exists(x => x == name)))
      {
        throw new IncorrectConfigurationException(s"Unable to find component config path for name: ${name}")
      }
    val componentConfig = integration.getConfig(name)
    val topic = componentConfig.getString(IntegrationConstant.TOPIC)
//    val group = componentConfig.getString(IntegrationConstant.GROUP)
    val group = ""
    val autoCommit = componentConfig.getBoolean(IntegrationConstant.AUTO_COMMIT)

    val result = IntegrationConfigProperty(servers,topic,group,autoCommit)
    result
  }

  object IntegrationConstant {
    val MAIN_CONFIG = "integration"
    val SERVERS = "bootstrap.servers"
    val COMPONENTS = "components"
    val TOPIC = "topic"
    val GROUP = "group"
    val AUTO_COMMIT = "autoCommit"

  }
}
case class IntegrationConfigProperty(servers: String, topic: String, groupId: String, autoCommit: Boolean)
