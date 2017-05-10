package io

/**
  * Created by marcin on 5/7/17.
  */

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.collection.mutable.ListBuffer
import scala.util.Random
import scala.util.control.Exception.ignoring

import communication._

class DownloaderActor(fileManifesto: FileManifesto, implicit val timeoutDuration: FiniteDuration) extends Actor {
  val uploaders: ListBuffer[ActorRef] = ListBuffer()
  val builder = new FileBuilder(fileManifesto)
  implicit val timeout = Timeout(timeoutDuration)

  //  println(s"manifesto.chunksize = ${fileManifesto.chunkSize}")
  //  println(s"manifesto.chunkcount = ${fileManifesto.chunkCount}")

  override def receive: PartialFunction[Any, Unit] = {

    case chunk: Chunk =>
      if (!uploaders.contains(sender)) uploaders += sender
      //      println(s"received chunk ${chunk.id}")
      builder.accept(chunk)
      context.setReceiveTimeout(timeoutDuration)

    case ReceiveTimeout =>
      println("timeout received")
      if (builder.missingChunks.isEmpty) {
        println("ready to build")
        context.setReceiveTimeout(Duration.Undefined)
        builder.build()
        context.stop(self)
      } else if (uploaders.nonEmpty) {
        println("not ready to build yet")
        println(s"${builder.missingChunks} are missing")
        val randomUploader = Random.shuffle(uploaders).head
        builder.missingChunks.foreach { i =>
          ignoring(classOf[java.util.concurrent.TimeoutException]) {
            builder.accept(Await.result((randomUploader ? i).mapTo[Chunk], timeoutDuration))
          }
        }
      }
  }


}
