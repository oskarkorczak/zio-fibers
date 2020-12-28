import Dependencies._

ThisBuild / organization := "com.zio.fiber"
ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "0.0.1-SNAPSHOT"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Wunused:_",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)

lazy val `zio-fibers` =
  project
    .in(file("."))
    .settings(
      name := "zio-fibers",
      libraryDependencies ++= Seq(
        zio
      ),
      libraryDependencies ++= Seq(
        scalaTest
      ).map(_ % Test),
      Compile / console / scalacOptions --= Seq(
        "-Wunused:_",
        "-Xfatal-warnings"
      ),
      Test / console / scalacOptions :=
        (Compile / console / scalacOptions).value
    )
