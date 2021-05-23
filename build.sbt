name := "agario_clone"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.6.3",
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.3",
  "com.typesafe.akka" %% "akka-remote" % "2.6.3",
  "com.typesafe.akka" %% "akka-stream" % "2.6.3",
  "com.typesafe.akka" %% "akka-http" % "10.2.4",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",
  "commons-io" % "commons-io" % "2.6",
  "io.netty" % "netty" % "3.10.6.Final",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "commons-daemon" % "commons-daemon" % "1.0.15",
)
