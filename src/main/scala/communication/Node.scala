package communication

import java.io.File

import akka.actor._
import io.{DownloaderActor, UploaderActor}

import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit.SECONDS


/**
  * Created by marcin on 5/8/17.
  */
class Node(chunkSize: Int) extends Actor {
  implicit val timeout: FiniteDuration = FiniteDuration(1, SECONDS)
  val spokesman: ActorRef = context.system.actorOf(Props[Spokesman], name = "spokesman")

  override def receive: Receive = {

    case Request(filename: String) =>
      sender ! FileManifesto(new File(filename), chunkSize, filename + "(1)")

    case fileManifesto: FileManifesto =>
      val downloader = context.system.actorOf(Props(classOf[DownloaderActor], fileManifesto, timeout), name = "downloader")
      println(downloader.path)
      val confirmation = Confirmation(fileManifesto)
      sender.tell(confirmation, downloader)

    case Confirmation(fileManifesto) =>
      val uploader = context.system.actorOf(
        Props(classOf[UploaderActor], sender, fileManifesto), name = "uploader")


    case selection: ActorSelection =>
      selection ! Request(".gitignore")

    case _ =>
      println("WTF")


  }
}
