package io

/**
  * Created by marcin on 5/7/17.
  */

import java.io._

import akka.actor._
import communication._
import util.ChunkNotFoundException

import scala.collection.mutable.ListBuffer

class DownloaderActor(manifesto: FileManifesto) extends Actor {
  val chunksBuffer: ListBuffer[Chunk] = ListBuffer()

  override def receive: PartialFunction[Any, Unit] = {

    case chunk: Chunk =>
      println(s"received chunk ${chunk.id}")
      if (!chunksBuffer.exists(_.id == chunk.id)) chunksBuffer += chunk
      if (chunksBuffer.length == manifesto.chunkCount) {
        try {
          build()
        } catch {
          case ChunkNotFoundException(chunkId) => println("Building Error!")
        }
      }
    case msg =>
      println(s"Received $msg")


  }

  def build(): Unit = {
    println("Builder started")
    val chunks = chunksBuffer.toList
    println("Chunks listified")
    var name = manifesto.name

    while (new File(name).exists()) {
      name += "(1)"
    }
    println(s"Trying to build $name")
    val newFile = new File(name)
    val writer = new BufferedOutputStream(new FileOutputStream(newFile))


    for (i <- 0 until manifesto.chunkCount) {
      try {
        writer.write(chunks.filter(c => c.id == i).head.content)
      } catch {
        case e: java.util.NoSuchElementException =>
          println(e)
          newFile.delete()
          throw ChunkNotFoundException(i)
      }
    }
    writer.close()
  }
}
