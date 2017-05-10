package io

import java.io.{BufferedOutputStream, File, FileOutputStream}

import communication.FileManifesto

import scala.collection.mutable.ListBuffer

/**
  * Created by marcin on 5/10/17.
  */
class FileBuilder(fileManifesto: FileManifesto) {

  private val chunksBuffer: ListBuffer[Chunk] = ListBuffer()

  def chunks: List[Chunk] = chunksBuffer.toList.sortBy(_.id)

  def accept(chunk: Chunk): Unit = {
    if (!chunksBuffer.exists(_.id == chunk.id)) chunksBuffer += chunk
  }


  def missingChunks: List[Int] = {
    val missing: ListBuffer[Int] = ListBuffer()
    for (i <- 0 until fileManifesto.chunkCount) {
      if (!chunksBuffer.exists(_.id == i)) missing += i
    }
    missing.toList
  }


  def build(): Unit = {
    //    println(s"build: manifesto.chunkcount = ${fileManifesto.chunkCount}")
    //    println(s"build: chunks = ${chunks.length}")
    var name = fileManifesto.name
    println(s"Trying to build $name")
    val newFile = new File(name)
    val writer = new BufferedOutputStream(new FileOutputStream(newFile))

    (0 until fileManifesto.chunkCount)
      .foreach { i => writer.write(chunks.filter(_.id == i).head.content) }

    writer.close()
  }

}
