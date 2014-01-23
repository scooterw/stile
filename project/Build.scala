import sbt._
import Keys._
import Tests._
import scala.language.postfixOps

object BuildSettings {
  val buildOrganization = "com.github.scooterw"
  val buildVersion = "0.1-SNAPSHOT"
  val buildScalaVersion = "2.10.3"

  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt := ShellPrompt.buildShellPrompt
  )
}

object ShellPrompt {
  object devnull extends ProcessLogger {
    def info(s: => String) {}
    def error(s: => String) {}
    def buffer[T](f: => T): T = f
  }

  def currentBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## "
  )

  val buildShellPrompt = {
    (state: State) => {
      val currentProject = Project.extract(state).currentProject.id
      "%s:%s:%s> ".format(
        currentProject, currentBranch, BuildSettings.buildVersion  
      )
    }
  }
}

object Resolvers {}

object Dependencies {
  val slickVersion = "2.0.0"
  val jdbcSqliteVersion = "3.7.2"
  val jzlibVersion = "1.1.3"
  val specs2Version = "2.3.7"

  val slick = "com.typesafe.slick" %% "slick" % slickVersion
  val jdbcSqlite = "org.xerial" % "sqlite-jdbc" % jdbcSqliteVersion
  val jzlib = "com.jcraft" % "jzlib" % jzlibVersion
  val specs2 = "org.specs2" %% "specs2" % specs2Version % "test"
}

object StileBuild extends Build {
  import Resolvers._
  import Dependencies._
  import BuildSettings._

  val commonDeps = Seq(
    specs2
  )

  val tileDeps = Seq(
    slick,
    jdbcSqlite,
    jzlib,
    specs2
  )

  lazy val main = Project(
    "main",
    file("."),
    settings = buildSettings
  ) dependsOn(common, tile) aggregate(common, tile)

  lazy val common = Project(
    "common",
    file("stile-common"),
    settings = buildSettings ++ Seq(libraryDependencies ++= commonDeps)
  )

  lazy val tile = Project(
    "tile",
    file("stile-tile"),
    settings = buildSettings ++ Seq(libraryDependencies ++= tileDeps)
  ) dependsOn(common)
}

