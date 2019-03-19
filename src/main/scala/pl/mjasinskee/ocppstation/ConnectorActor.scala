package pl.mjasinskee.ocppstation

import java.time.ZonedDateTime

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import akka.actor.{Actor, ActorLogging}
import com.thenewmotion.ocpp.json.api.Ocpp1XJsonClient
import com.thenewmotion.ocpp.messages.v1x.{ChargePointStatus, ConnectorScope, OccupancyKind, StartTransactionReq, StatusNotificationReq}

import scala.language.postfixOps

class ConnectorActor(client: Ocpp1XJsonClient)  extends Actor with ActorLogging{
  import ConnectorActor._

  override def receive= {
    case Plug => client.send(StatusNotificationReq(
      ConnectorScope(1),
      ChargePointStatus.Occupied(
        Some(OccupancyKind.Charging),
        info = None
      ),
      Some(ZonedDateTime.now()),
      None
    )).map {res => log.info("Response from CentralSystem: ", res.toString)}
    case RemoteStartRequested => client.send(StartTransactionReq(
      ConnectorScope(1),
      "rfid",
      ZonedDateTime.now(),
      100,
      None)).map {res => log.info("Response from CentralSystemn: ", res.toString)}
  }

}

object ConnectorActor {

  case object SendMeterValue

  sealed trait Action
  case object Plug extends Action
  case object Unplug extends Action
  case object RemoteStartRequested extends Action

}