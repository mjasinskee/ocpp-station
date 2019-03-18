name := "ocpp-station"

version := "0.1"

scalaVersion := "2.12.8"

resolvers += "The New Motion Repository" at "http://nexus.thenewmotion.com/content/repositories/releases-public"

libraryDependencies += "com.thenewmotion.ocpp" % "ocpp-j-api_2.12"  % "9.1.0"