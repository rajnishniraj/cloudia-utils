package communication

import java.io.File

import akka.actor._
import io.{DownloaderActor, UploaderActor}
import util.Tracer

import scala.concurrent.duration.FiniteDuration

/**
  * Created by marcin on 5/8/17.
  */
class Cloudia(implicit val chunkSize: Long, implicit val host: String, implicit val port: Int) extends Actor {


  override def receive: Receive = {

    case Request(filename) =>
      println(s"Received request from ${sender().path}. Sending manifesto.")
      sender ! FileManifesto(new File(filename), chunkSize)

    case fileManifesto: FileManifesto =>
      println(s"Received manifesto from ${sender().path}. Sending confirmation.")
      val downloader = context.system.actorOf(Props(classOf[DownloaderActor], sender()), name = "downloader")
      println(downloader.path)
      val confirmation = Confirmation(context.system.name, host, port, "downloader")
      downloader ! confirmation

    case Confirmation(system, host, port, downloaderName) =>
      println(s"Received confirmation from ${sender().path}. Preparing uploader.")
      val uploader = context.system.actorOf(
        Props(classOf[UploaderActor], sender()), name = "uploader")
      uploader ! List(1, 2, 3, 4)

    case selection: ActorSelection =>
      selection ! Request(".gitignore")

    case _ =>
      println("WTF")


  }
}
