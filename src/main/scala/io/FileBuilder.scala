package io

import java.io.{BufferedOutputStream, File, FileOutputStream}

import communication.FileManifest

import scala.collection.mutable.ListBuffer

/**
  * Created by marcin on 5/10/17.
  */
class FileBuilder(fileManifest: FileManifest)(implicit homeDirPath: String) {

  private val chunksBuffer: ListBuffer[Chunk] = ListBuffer()

  def chunks: List[Chunk] = chunksBuffer.toList.sortBy(_.id)

  def accept(chunk: Chunk): Unit = {
    if (!chunksBuffer.exists(_.id == chunk.id)) chunksBuffer += chunk
  }


  def missingChunks: List[Int] = {
    val missing: ListBuffer[Int] = ListBuffer()
    for (i <- 0 until fileManifest.chunkCount) {
      if (!chunksBuffer.exists(_.id == i)) missing += i
    }
    missing.toList
  }


  def build(): Unit = {
    val name = fileManifest.fileIndex.path
    val newFile = new File(homeDirPath + "/" + name)
    newFile.getParentFile.mkdirs()
    val writer = new BufferedOutputStream(new FileOutputStream(newFile))

    (0 until fileManifest.chunkCount)
      .foreach { i => writer.write(chunks.filter(_.id == i).head.content) }
    writer.close()
    newFile.setWritable(fileManifest.fileIndex.writable)
    newFile.setReadable(fileManifest.fileIndex.readable)
    newFile.setExecutable(fileManifest.fileIndex.executable)
  }

}
