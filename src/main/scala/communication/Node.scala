package communication

import java.io.File

import akka.actor._
import io.{DownloaderActor, UploaderActor}

import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit.SECONDS


/**
  * Created by marcin on 5/8/17.
  */
class Node(implicit val chunkSize: Int,
           implicit val host: String,
           implicit val port: Int
          ) extends Actor {
  println(s"chunksize = $chunkSize")
  implicit val timeout: FiniteDuration = FiniteDuration(1, SECONDS)


  override def receive: Receive = {

    case Request(filename) =>
      println(s"Received request from ${sender().path}. Sending manifesto.")
      sender ! FileManifesto(new File(filename), chunkSize, filename + "(1)")

    case fileManifesto: FileManifesto =>
      //      println(s"Received manifesto from ${sender.path}. Sending confirmation.")
      //      println(s"mainfesto.chunksize = ${fileManifesto.chunkSize}")
      //      println(s"mainfesto.chunkcount = ${fileManifesto.chunkCount}")
      val downloader = context.system.actorOf(Props(classOf[DownloaderActor], fileManifesto, timeout), name = "downloader")
      println(downloader.path)
      val confirmation = Confirmation(fileManifesto)
      sender.tell(confirmation, downloader)

    case Confirmation(fileManifesto) =>
      //      println(s"Received confirmation from ${sender().path}. Preparing uploader.")
      val uploader = context.system.actorOf(
        Props(classOf[UploaderActor], sender, fileManifesto), name = "uploader")


    case selection: ActorSelection =>
      selection ! Request(".gitignore")

    case _ =>
      println("WTF")


  }
}
