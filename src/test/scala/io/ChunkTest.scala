package io

/**
  * Created by mike on 6/29/17.
  */

import java.io.File

import org.scalatest._

class ChunkTest extends FlatSpec {
  "Chunkifier" should "return right # of chunks for non-empty file" in {
    val file = new File("src/test/resources/chunkifierTest")
    val fileChunksCount = 40
    val chunksCount = Chunkifier.chunksCount(file, 100)
    assert(fileChunksCount == chunksCount)
  }

  it should "return 0 # of chunks for empty file " in {
    val file = new File("src/test/resources/emptyTestFile")
    val fileChunksCount = 0
    val chunksCount = Chunkifier.chunksCount(file, 100)
    assert(fileChunksCount == chunksCount)
  }
}
