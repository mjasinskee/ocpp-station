package pl.mjasinskee.ocppstation

import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

import akka.actor._
import com.thenewmotion.ocpp.json.api.Ocpp1XJsonClient
import com.thenewmotion.ocpp.messages.v1x.ChargePointStatus.Available
import com.thenewmotion.ocpp.messages.v1x.{BootNotificationReq, ChargePointScope, HeartbeatReq, StatusNotificationReq}
import pl.mjasinskee.ocppstation.CentralSystemActor.{RemoteStartRequested, RemoteStopRequested}

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.language.postfixOps

class ChargerActor(client: Ocpp1XJsonClient) extends Actor with ActorLogging {
  import ChargerActor._

  override def preStart() {

    for {
      _ <- client.send(BootNotificationReq(
        chargePointVendor = "The New Motion",
        chargePointModel = "Lolo 47.6",
        chargePointSerialNumber = Some("123456"),
        chargeBoxSerialNumber = None,
        firmwareVersion = None,
        iccid = None,
        imsi = None,
        meterType = None,
        meterSerialNumber = None)).map { res => log.info(res.toString) }

      _ <- client.send(HeartbeatReq).map { res => log.info(res.toString) }

      _ <- client.send(StatusNotificationReq(
        ChargePointScope,
        Available(),
        Some(ZonedDateTime.now()),
        None
      )).map { res => log.info(res.toString) }
    } yield()

    context.system.scheduler.schedule(initialDelay = Duration(10, TimeUnit.SECONDS), interval = Duration(60, TimeUnit.SECONDS), self, Heartbeat)

  }


  override def receive = {
    case RemoteStartRequested => log.info("RemoteStart requested")
    case RemoteStopRequested => log.info("RemoteStop received")
    case Heartbeat => {
      log.info("Heartbeat requested")
      client.send(HeartbeatReq).map(res => log.info(res.toString))
    }
  }
}

object ChargerActor {
  sealed trait Action
  case object Heartbeat extends Action

  sealed trait CentralSystemAction
  case class RemoteStartTransaction(rfid: String, connector: Int) extends CentralSystemAction
  case class RemoteStopTransaction(transactionId: Int) extends CentralSystemAction

}
