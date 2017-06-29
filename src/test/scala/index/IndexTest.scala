package index

import java.io.File

import org.scalatest.FlatSpec
import util.NotAFileException

/**
  * Created by mike on 6/29/17.
  */
class IndexTest extends FlatSpec {
  implicit val homeDirPath = "/home/mike/Programming/scala/"

  "FileIndex" should "acquire correct file rights" in {
    val fileIndex = new FileIndex(new File("src/test/resources/chunkifierTest"))
    assert(fileIndex.writable)
    assert(fileIndex.readable)
    assert(!fileIndex.executable)
  }

  it should "throw NotAFileException when constructed on directory" in {
    intercept[NotAFileException] {
      val fileIndex = new FileIndex(new File("src/test"))
    }
  }

  it should "throw NotAFileException when constructed on nonexistent file" in {
    intercept[NotAFileException] {
      val fileIndex = new FileIndex(new File("src/test/nonexistentfile"))
    }
  }

  "DirectoryIndex" should "find FileIndex of file that was searched for" in {
    val fileIndex = new FileIndex(new File("src/test/resources/chunkifierTest"))
    val directoryIndex = new DirectoryIndex(new File("src/test/resources"))
    val fileIndexes: List[Index] = directoryIndex.find("chunkifierTest")
    assert(fileIndexes.nonEmpty)
    assert(fileIndexes.head.path == fileIndex.path)
  }

  it should "return empty list of Indexes when searching for nonexistent file" in {
    val directoryIndex = new DirectoryIndex(new File("src/test/resources"))
    val fileIndexes: List[Index] = directoryIndex.find("nonexistentFile")
    assert(fileIndexes.isEmpty)
  }
}
