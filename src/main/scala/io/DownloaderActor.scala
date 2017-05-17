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

private class DownloaderActor(fileManifesto: FileManifesto, timeoutDuration: FiniteDuration)
(implicit homeDirPath: String)
  extends Actor {
  val uploaders: ListBuffer[ActorRef] = ListBuffer()
  val builder = new FileBuilder(fileManifesto)
  implicit val timeout = Timeout(timeoutDuration)
  val spokesman: ActorSelection = context.system.actorSelection("user/spokesman")
//  spokesman ! s"manifesto.chunksize = ${fileManifesto.chunkSize}"


  override def receive: PartialFunction[Any, Unit] = {

    case chunk: Chunk =>
      if (!uploaders.contains(sender)) uploaders += sender
      builder.accept(chunk)
      context.setReceiveTimeout(timeoutDuration)

    case ReceiveTimeout =>
      if (builder.missingChunks.isEmpty) {
        context.setReceiveTimeout(Duration.Undefined)
        builder.build()
        uploaders.foreach(_ ! PoisonPill)
        self ! PoisonPill
      } else if (uploaders.nonEmpty) {
        val randomUploader = Random.shuffle(uploaders).head
        builder.missingChunks.foreach { chunkId =>
          ignoring(classOf[java.util.concurrent.TimeoutException]) {
            builder.accept(Await.result(
              (randomUploader ? Request(chunkId))
                .mapTo[Chunk], timeoutDuration))
          }
        }
      }
  }
}

object DownloaderActor{
  def props(fileManifesto: FileManifesto, timeoutDuration: FiniteDuration)
           (implicit homeDirPath: String): Props = {
    Props(new DownloaderActor(fileManifesto, timeoutDuration))

  }

}
