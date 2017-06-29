package io

import java.io.File

import communication.FileManifest
import index.FileIndex
import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by mike on 6/29/17.
  */
class ChunkifyAndBuildTest extends FlatSpec with Matchers {
  implicit val homeDirPath = "/home/mike/Programming/scala/"

  "Chunkifier and FileBuilder" should "chunkify and rebuild file properly" in {
    val originalFile = new File("src/test/resources/chunkifierTest")
    val chunksList: List[Chunk] = Chunkifier(100, originalFile)
    val fileManifest = FileManifest(new FileIndex(originalFile), 100)
    val fileBuilder = new FileBuilder(fileManifest)
    chunksList.foreach(chunk => fileBuilder.accept(chunk))
    fileBuilder.build()
    val newChunksList: List[Chunk] = Chunkifier(100, originalFile)

    chunksList.indices.foreach { i => chunksList(i).content should equal(newChunksList(i).content) }
  }
}
