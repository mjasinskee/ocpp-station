package pl.mjasinskee.ocppstation

import java.net.URI

import akka.actor.Props
import com.thenewmotion.ocpp.json.api.client.OcppJsonClient
import com.thenewmotion.ocpp.{Version, Version1X}

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object StationApp {
  def main(args: Array[String]) {
    val chargerId = args.headOption.getOrElse("NUON_0001")
    val centralSystemUri =
      if (args.length >= 2) args(1) else "ws://localhost:8080/ocppws"
    val versions =
      if (args.length >= 3)
        args(2).split(",").map { version =>
          Version.withName(version).getOrElse(sys.error(s"Unrecognized version(s): ${args(2)}"))
        }.toSeq.collect({ case v: Version1X => v })
      else
        Seq(Version.V16)

    val ocppJsonClient = OcppJsonClient.forVersion1x(chargerId, new URI(centralSystemUri), versions) {
      new DefaultChargingPoint
    }

    system.actorOf(Props(new ChargerActor(ocppJsonClient)))

    ocppJsonClient.onClose.foreach(_ => println("OCPP connection closed"))

    println(s"Connected using OCPP version ${ocppJsonClient.ocppVersion}")
  }

}
