package io

import akka.actor._
import communication.{FileManifesto, Request}

/**
  * Created by marcin on 5/7/17.
  */

class UploaderActor(downloaderActor: ActorRef, fileManifesto: FileManifesto) extends Actor {
  //  println(s"manifesto.chunksize = ${fileManifesto.chunkSize}")
  //  println(s"manifesto.chunkcount = ${fileManifesto.chunkCount}")

  override def preStart(): Unit = {
    Chunkifier(fileManifesto.chunkSize, fileManifesto.file).foreach {
      chunk =>
        downloaderActor ! chunk
    }
    downloaderActor ! ReceiveTimeout
  }


  override def receive: PartialFunction[Any, Unit] = {
    case Request(missingChunkId: Int) =>
      sender ! Chunkifier(fileManifesto.chunkSize, fileManifesto.file)(missingChunkId)
  }

}
