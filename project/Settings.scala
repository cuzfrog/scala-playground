import sbt.Keys._
import sbt._
import MyTasks._

object Settings {
  val commonSettings = Seq(
    resolvers ++= Seq(
      Resolver.mavenLocal,
      "bintray-cuzfrog-maven" at "http://dl.bintray.com/cuzfrog/maven",
      "Artima Maven Repository" at "http://repo.artima.com/releases",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
      "spray repo" at "http://repo.spray.io"
    ),
    organization := "com.github.cuzfrog",
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq(
      "-Xlint",
      "-unchecked",
      "-deprecation",
      "-feature"),
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.7" % "provided",
      "junit" % "junit" % "4.12" % "test",
      "com.novocode" % "junit-interface" % "0.11" % "test->default",
      "org.scalacheck" %% "scalacheck" % "1.13.2" % "test",
      "org.mockito" % "mockito-core" % "1.10.19" % "test"
    ),
    logBuffered in Test := false,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v", "-q", "-a"),
    parallelExecution in Test := false,
    licenses += ("Apache-2.0", url("https://opensource.org/licenses/Apache-2.0"))
  )

  private val bintrayUser = System.getenv("BINTRAY_USER")
  private val bintrayPass = System.getenv("BINTRAY_PASS")

  val publicationSettings = Seq(
    publishTo := Some("My Bintray" at s"https://api.bintray.com/maven/cuzfrog/maven/${(name in ThisProject).value}/;publish=1"),
    credentials += Credentials("Bintray API Realm", "api.bintray.com", bintrayUser, bintrayPass)
  )

  val readmeVersionSettings = Seq(
    (compile in Compile) := ((compile in Compile) dependsOn versionReadme).value,
    versionReadme := {
      val contents = IO.read(file("README.md"))
      val projectName = (name in ThisProject).value
      val regex =s"""(?<=libraryDependencies \+= "com\.github\.cuzfrog" %% "$projectName" % ")[\d\w\-\.]+(?=")"""
      val newContents = contents.replaceAll(regex, version.value)
      IO.write(file("README.md"), newContents)
    }
  )
}