import sbt._
import Keys._

object Common {
  val appVersion = "1.0"

  lazy val copyDependencies = TaskKey[Unit]("copy-dependencies")

  def copyDepTask = copyDependencies <<= (update, crossTarget, scalaVersion) map {
    (updateReport, out, scalaVer) =>
      updateReport.allFiles foreach { srcPath =>
        val destPath = out / "lib" / srcPath.getName
        IO.copyFile(srcPath, destPath, preserveLastModified=true)
      }
  }

  val settings: Seq[Def.Setting[_]] = Seq(
    version := appVersion,
    scalaVersion := "2.12.0",
    javacOptions ++= Seq("-source", "1.7", "-target", "1.7"), //, "-Xmx2G"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-Xfatal-warnings",
      "-feature",
      "-language:_"
    ),
    resolvers += Opts.resolver.mavenLocalFile,
    copyDepTask,
    resolvers ++= Seq(DefaultMavenRepository,
                      Resolver.defaultLocal,
                      Resolver.mavenLocal,
                      Resolver.sonatypeRepo("releases"),
                      Resolver.sonatypeRepo("snapshots"),
                      Resolver.typesafeRepo("releases"),
                      Resolver.bintrayRepo("websudos", "oss-releases"),
                      Resolver.bintrayRepo("outworkers", "oss-releases"),
                      Resolver.bintrayRepo("cakesolutions", "maven"),
                      Resolver.bintrayRepo("hseeberger", "maven")
    )
  )
}