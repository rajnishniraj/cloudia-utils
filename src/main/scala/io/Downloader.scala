package io

/**
  * Created by marcin on 5/7/17.
  */

import akka.actor._
import communication._

import scala.collection.mutable.ListBuffer

class DownloaderActor(manifesto: FileManifesto, remoteWaiter: ActorRef) extends Actor {
  val chunks: ListBuffer[Chunk] = ListBuffer()


  override def receive: PartialFunction[Any, Unit] = {

    case confirmation: Confirmation =>
      println(s"Sending confirmation to ${remoteWaiter.path}")
      remoteWaiter ! confirmation
    case msg =>
      println(s"Received $msg")
    case chunk: Chunk =>
      println("received chunk")
      if(!chunks.exists(_.id == chunk.id)) chunks.insert(1, chunk)


  }
}
