package iot.pood.base.integration

import com.typesafe.config.Config
import iot.pood.base.config.Configurator
import iot.pood.base.exception.Exceptions.IncorrectConfigurationException
import iot.pood.base.log.Log

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}
/**
  * Created by rafik on 22.9.2017.
  */
object IntegrationConfig extends Log{

  var integrationConfig:Option[IntegrationConfig] = None

  def apply(config: Config):IntegrationConfig = {
    integrationConfig match {
      case None => {
        integrationConfig = Some(new IntegrationConfig(config))
        integrationConfig.get
      }
      case Some(_) => integrationConfig.get
    }
  }

  object IntegrationConstants {
    val MAIN_CONFIG = "integration"
    val SERVERS = "bootstrap.servers"
    val COMPONENTS = "components"
    val TOPIC = "topic"
    val GROUP = "group"
    val AUTO_COMMIT = "autoCommit"

  }

  class IntegrationConfig(config: Config) extends Configurator[IntegrationProperty]{

    val integrationConfig = config.getConfig(IntegrationConstants.MAIN_CONFIG)

    private[this] val integrationProperty:Map[String, IntegrationProperty] = load()


    override def appConfig: Config = config


    override def componentConfig: Config = integrationConfig

    override def get(name: String): IntegrationProperty = {
      integrationProperty.get(name) match {
        case None => throw new IncorrectConfigurationException(s"Unable to find integration config for component: $name")
        case Some(value)=> value
      }
    }

    private def load() = {

      val server = integrationConfig.getString(IntegrationConstants.SERVERS)
      val components = integrationConfig.getStringList(IntegrationConstants.COMPONENTS).asScala.toList
      Map(components map { name => (name,createProperty(integrationConfig,name,server))} : _*)
    }

    private def createProperty(config: Config,name: String,server: String) = {

      Try({
        val component = config.getConfig(name)
        IntegrationProperty(server,component.getString(IntegrationConstants.TOPIC)
          ,Try({component.getString(IntegrationConstants.GROUP)}) match {
            case Success(x) => Some(x)
            case Failure(y) => Some("")
          },component.getBoolean(IntegrationConstants.AUTO_COMMIT))
      }) match {
        case Success(x) => x
        case Failure(y) => {
          log.error("Unable to parse integration configuration:")
          throw new IncorrectConfigurationException(
            """
              |Example:
              |integration {
              |  bootstrap.servers = "kafka:9092"
              |  components = ["data","command"]
              |  data {
              |    topic = "v1.data"
              |    autoCommit = false
              |  }
              |  command {
              |    topic = "v1.command"
              |    autoCommit = false
              |  }
              |}
            """.stripMargin)
        }
      }
    }
  }

  case class IntegrationProperty(servers: String, topic: String, groupId: Option[String] = None, autoCommit: Boolean)

}
