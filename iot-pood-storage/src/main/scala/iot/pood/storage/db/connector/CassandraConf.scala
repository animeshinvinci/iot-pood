package iot.pood.storage.db.connector

import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConverters

/**
  * Created by rafik on 7.8.2017.
  */
object CassandraConf {


  def apply(prefix: String,conf: Config): CassandraConf = {
    val config = conf.getConfig(prefix)
    val stringList = config.getStringList("hosts")
    CassandraConf(JavaConverters.asScalaIterator(stringList.iterator()).toSeq,
      config.getString("keyspace"),
      config.getInt("replication_factor"),
      config.getString("username"),
      config.getString("password"))
  }



  case class CassandraConf(hosts:Seq[String],
                           keyspaceName: String,
                           replicationFactor: Int,
                           username: String,
                           password: String)

}
