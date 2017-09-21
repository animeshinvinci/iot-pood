import sbt._



lazy val util = (project in file("iot-pood-util"))
    .settings(Common.settings: _*)
    .settings(libraryDependencies ++= Dependencies.baseDependencies)


lazy val base = (project in file("iot-pood-base"))
    .settings(Common.settings: _*)
    .settings(libraryDependencies ++= Dependencies.baseDependencies)
    .dependsOn(util)

lazy val integration = (project in file("iot-pood-integration"))
  .settings(Common.settings: _*)
  .dependsOn(util,base)
  .settings(libraryDependencies ++=Dependencies.integrationDependencies)

lazy val mqtt = (project in file("iot-pood-mqtt"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++=Dependencies.iotMessagingDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base)

lazy val rules = (project in file("iot-pood-rules"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++= Dependencies.rulesEngineDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base,integration)

lazy val service = (project in file("iot-pood-service"))
    .settings(Common.settings: _*)
    .settings(libraryDependencies ++= Dependencies.serviceDependencies)
    .enablePlugins(sbtdocker.DockerPlugin)
    .dependsOn(util,base,integration)

lazy val storage = (project in file("iot-pood-storage"))
  .settings(Common.settings: _*)
  .settings(libraryDependencies ++=Dependencies.storageDependencies)
  .enablePlugins(sbtdocker.DockerPlugin)
  .dependsOn(util,base)

lazy val root = (project in file(".")).
  aggregate(base,util,service,integration,mqtt,storage,rules)


