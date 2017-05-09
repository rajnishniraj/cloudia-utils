package communication

import java.io.File

import akka.actor._
import io.{Chunkifier, DownloaderActor, UploaderActor}


/**
  * Created by marcin on 5/8/17.
  */
class Node(implicit val chunkSize: Int, implicit val host: String, implicit val port: Int) extends Actor {


  override def receive: Receive = {

    case Request(filename) =>
      println(s"Received request from ${sender().path}. Sending manifesto.")
      sender ! FileManifesto(new File(filename), chunkSize)

    case fileManifesto: FileManifesto =>
      println(s"Received manifesto from ${sender.path}. Sending confirmation.")
      val downloader = context.system.actorOf(Props(classOf[DownloaderActor], fileManifesto), name = "downloader")
      println(downloader.path)
      val confirmation = Confirmation(fileManifesto)
      sender.tell(confirmation, downloader)

    case Confirmation(FileManifesto(file, fileChunkSize, _))=>
      println(s"Received confirmation from ${sender().path}. Preparing uploader.")
      val uploader = context.system.actorOf(
        Props(classOf[UploaderActor], sender()), name = "uploader")
      uploader ! Chunkifier(fileChunkSize, file)

    case selection: ActorSelection =>
      selection ! Request(".gitignore")

    case _ =>
      println("WTF")


  }
}
