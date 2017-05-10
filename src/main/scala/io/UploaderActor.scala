package io

import akka.actor._
import communication.FileManifesto

/**
  * Created by marcin on 5/7/17.
  */

class UploaderActor(downloaderActor: ActorRef, fileManifesto: FileManifesto) extends Actor {
  //  println(s"manifesto.chunksize = ${fileManifesto.chunkSize}")
  //  println(s"manifesto.chunkcount = ${fileManifesto.chunkCount}")

  override def preStart(): Unit = {
    Chunkifier(fileManifesto.chunkSize, fileManifesto.file).foreach {
      chunk =>
        println(s"sending chunk ${chunk.id}")
        downloaderActor ! chunk
    }
    downloaderActor ! ReceiveTimeout
  }


  override def receive: PartialFunction[Any, Unit] = {
    case missingChunkId: Int =>
      println(s"Sending missing chunk #$missingChunkId")
      sender ! Chunkifier(fileManifesto.chunkSize, fileManifesto.file)(missingChunkId)
  }

}
