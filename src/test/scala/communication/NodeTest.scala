package communication

import java.io.File

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import index.{DirectoryIndex, FileIndex, Index}

import scala.concurrent.duration.{Duration, SECONDS}

/**
  * Created by mike on 6/29/17.
  */
class NodeTest() extends TestKit(ActorSystem("NodeTest")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  implicit val homeDirPath = "/home/mike/Programming/scala/Cloudia"


  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Node" must {
    "send back directory index if pinged" in {
      val node: ActorRef = system.actorOf(Node.props(homeDirPath))
      node ! Ping()
      expectMsgPF() {
        case DirectoryIndex(_) => ()
      }
    }
  }

  it must {
    "send back file manifest if one was requested" in {
      val node: ActorRef = system.actorOf(Node.props(homeDirPath))
      val fileIndex = new FileIndex(new File("src/test/resources/chunkifierTest"))
      node ! Request(fileIndex)
      expectMsgPF() {
        case FileManifest(_, _) => ()
      }
    }
  }
}
