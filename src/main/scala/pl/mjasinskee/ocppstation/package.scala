package pl.mjasinskee

import akka.actor.ActorSystem

package object ocppstation {

  implicit val system = ActorSystem("ocpp-simulator")

}
