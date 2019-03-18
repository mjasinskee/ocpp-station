package pl.mjasinskee.ocppstation

class ConnectorActor {

}

object ConnectorActor {
  val initialMeterValue = 100

  sealed trait State

  case object Available extends State

  case object Connected extends State

  case object Charging extends State

  sealed trait Action

  case object Plug extends Action

  case object Unplug extends Action

}