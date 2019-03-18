package pl.mjasinskee.ocppstation

import java.time.ZonedDateTime

import akka.actor._
import com.thenewmotion.ocpp.json.api.Ocpp1XJsonClient
import com.thenewmotion.ocpp.messages.v1x.ChargePointStatus.Available
import com.thenewmotion.ocpp.messages.v1x.{BootNotificationReq, ChargePointScope, ChargePointStatus, HeartbeatReq, StatusNotificationReq}

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

class ChargerActor(client: Ocpp1XJsonClient) extends Actor with LoggingFSM[ChargerActor.State, ChargerActor.Data] {

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
        meterSerialNumber = None)).map {
        res =>
          println(res)
      }

      _ <- client.send(HeartbeatReq).map {
        res => println(res)
      }

      _ <- client.send(StatusNotificationReq(
        ChargePointScope,
        Available(),
        Some(ZonedDateTime.now()),
        None
      )).map {
        res => println(res)
      }
    } yield ()
  }
}

object ChargerActor {

  sealed trait State

  sealed trait Data

  case object Available extends State

  case object Faulted extends State

  sealed trait Action

  sealed trait CentralSystemAction extends Action

  case class RemoteStartTransaction(rfid: String, connector: Int) extends CentralSystemAction

  case class RemoteStopTransaction(transactionId: Int) extends CentralSystemAction

}
