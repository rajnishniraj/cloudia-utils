package io

/**
  * Created by marcin on 5/7/17.
  */

import akka.actor._
import communication.Confirmation

class DownloaderActor(remoteWaiter: ActorRef) extends Actor {


  override def receive: PartialFunction[Any, Unit] = {
    case chunk: Chunk => println("received chunk")
    case confirmation: Confirmation =>
      println(s"Sending confirmation to ${remoteWaiter.path}")
      remoteWaiter ! confirmation
    case msg =>
      println(s"Received $msg")


  }
}
