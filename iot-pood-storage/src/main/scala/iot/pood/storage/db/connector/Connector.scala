package iot.pood.storage.db.connector

import com.outworkers.phantom
import com.outworkers.phantom.builder.serializers.KeySpaceSerializer
import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoint, ContactPoints}
import com.outworkers.phantom.dsl._
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters

/**
  * Created by rafik on 1.8.2017.
  */
object Connector extends AppConfig{

  lazy val cassandraConf = CassandraConf("cassandra",appConfig)

  lazy val testCassandraConf = CassandraConf("test_cassandra",appConfig)

  lazy val connector: CassandraConnection = ContactPoints(cassandraConf.hosts)
    .withClusterBuilder(_.withCredentials(cassandraConf.username,cassandraConf.password))
    .keySpace(keySpaceInitQuery)

  lazy val keySpaceInitQuery = KeySpaceSerializer(cassandraConf.keyspaceName).ifNotExists()
      .`with`(phantom.dsl.replication eqs SimpleStrategy.replication_factor(cassandraConf.replicationFactor))
        .and(durable_writes eqs true)

  lazy val testConnector: CassandraConnection = ContactPoint.embedded.noHeartbeat().keySpace(testCassandraConf.keyspaceName)

}
