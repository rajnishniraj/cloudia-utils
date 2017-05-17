package io

import akka.actor._
import communication.{FileManifesto, Request}

/**
  * Created by marcin on 5/7/17.
  */

private class UploaderActor(downloaderActor: ActorRef, fileManifesto: FileManifesto)(implicit homeDirPath: String) extends Actor {
  override def preStart(): Unit = {
    Chunkifier(fileManifesto.chunkSize, fileManifesto.fileIndex.handler).foreach {
      chunk =>
        downloaderActor ! chunk
    }
    downloaderActor ! ReceiveTimeout
  }


  override def receive: PartialFunction[Any, Unit] = {
    case Request(missingChunkId: Int) =>
      sender ! Chunkifier(fileManifesto.chunkSize, fileManifesto.fileIndex.handler)(missingChunkId)
  }

}

object UploaderActor{
  def props(downloaderActor: ActorRef, fileManifesto: FileManifesto)
           (implicit homeDirPath:String):Props ={
    Props(new UploaderActor(downloaderActor, fileManifesto))
  }
}
