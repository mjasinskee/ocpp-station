package pl.mjasinskee.ocppstation

import akka.actor.{Actor, ActorLogging, Props}
import com.thenewmotion.ocpp.messages.v1x.{CancelReservationReq, CancelReservationRes, ChangeAvailabilityReq, ChangeAvailabilityRes, ChangeConfigurationReq, ChangeConfigurationRes, ChargePoint, ChargePointDataTransferReq, ChargePointDataTransferRes, ClearCacheRes, ClearChargingProfileReq, ClearChargingProfileRes, GetCompositeScheduleReq, GetCompositeScheduleRes, GetConfigurationReq, GetConfigurationRes, GetDiagnosticsReq, GetDiagnosticsRes, GetLocalListVersionRes, RemoteStartTransactionReq, RemoteStartTransactionRes, RemoteStopTransactionReq, RemoteStopTransactionRes, ReserveNowReq, ReserveNowRes, ResetReq, ResetRes, SendLocalListReq, SendLocalListRes, SetChargingProfileReq, SetChargingProfileRes, TriggerMessageReq, TriggerMessageRes, UnlockConnectorReq, UnlockConnectorRes, UpdateFirmwareReq}

import scala.concurrent.Future

class DefaultChargingPoint extends ChargePoint {
  import CentralSystemActor._
  val actor = system.actorOf(Props(new CentralSystemActor()))

  override def remoteStartTransaction(req: RemoteStartTransactionReq): Future[RemoteStartTransactionRes] = {
    actor ! RemoteStopRequested
    Future.successful(RemoteStartTransactionRes(accepted = true))
  }

  override def remoteStopTransaction(req: RemoteStopTransactionReq): Future[RemoteStopTransactionRes] = {
    actor ! RemoteStopRequested
    Future.successful(RemoteStopTransactionRes(accepted = true))
  }

  override def unlockConnector(req: UnlockConnectorReq): Future[UnlockConnectorRes] = ???

  override def getDiagnostics(req: GetDiagnosticsReq): Future[GetDiagnosticsRes] = ???

  override def changeConfiguration(req: ChangeConfigurationReq): Future[ChangeConfigurationRes] = ???

  override def getConfiguration(req: GetConfigurationReq): Future[GetConfigurationRes] = ???

  override def changeAvailability(req: ChangeAvailabilityReq): Future[ChangeAvailabilityRes] = ???

  override def clearCache: Future[ClearCacheRes] = ???

  override def reset(req: ResetReq): Future[ResetRes] = ???

  override def updateFirmware(req: UpdateFirmwareReq): Future[Unit] = ???

  override def sendLocalList(req: SendLocalListReq): Future[SendLocalListRes] = ???

  override def getLocalListVersion: Future[GetLocalListVersionRes] = ???

  override def dataTransfer(req: ChargePointDataTransferReq): Future[ChargePointDataTransferRes] = ???

  override def reserveNow(req: ReserveNowReq): Future[ReserveNowRes] = ???

  override def cancelReservation(req: CancelReservationReq): Future[CancelReservationRes] = ???

  override def clearChargingProfile(req: ClearChargingProfileReq): Future[ClearChargingProfileRes] = ???

  override def getCompositeSchedule(req: GetCompositeScheduleReq): Future[GetCompositeScheduleRes] = ???

  override def setChargingProfile(req: SetChargingProfileReq): Future[SetChargingProfileRes] = ???

  override def triggerMessage(req: TriggerMessageReq): Future[TriggerMessageRes] = ???
}

class CentralSystemActor extends Actor with ActorLogging{

  import CentralSystemActor._

  override def preStart() {
    log.info("CentralSystem Actor initialized")
  }

  override def receive: Receive = {
    case RemoteStartRequested => log.info("CentralSystem Actor handling RemoteStartRequested")
    case RemoteStopRequested => log.info("CentralSystem Actor handling RemoteStopRequested")
  }

}

object CentralSystemActor {

  sealed trait Action
  case class RemoteStartRequested() extends Action
  case class RemoteStopRequested() extends Action

}
