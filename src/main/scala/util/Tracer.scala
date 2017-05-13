package util

import akka.actor.{ActorRef, ActorSelection, ActorSystem}

/**
  * Created by marcin on 5/8/17.
  */
object Tracer {
  def actorAt(actorSystem: ActorSystem, system :String, host:String, port:Int, downloaderName:String): ActorSelection = {
    val protocol = "akka.tcp"
    val address = s"$protocol://$system@$host:$port/user/$downloaderName"
    actorSystem.actorSelection(address)
  }



}
