package io

import java.io.File
import java.nio.file.{Path, Files}
import scala.math.ceil


/**
  * Created by marcin on 5/7/17.
  */
case class Chunk(id: Int, content: Array[Byte]) extends Serializable

/** Slices files into chunks of given size, counts chunks in file. **/
object Chunkifier {

  def apply(implicit chunkSize: Int, file: File): List[Chunk] = {
    val src: Array[Byte] = Files.readAllBytes(file.toPath)
    val chunks = for (i <- 0 until chunksCount(file, chunkSize))
      yield Chunk(i, trySlice(src, i * chunkSize, (i + 1) * chunkSize))
    chunks.toList
  }

  def chunksCount(file: File, chunkSize: Int): Int = ceil(file.length.toDouble / chunkSize).toInt

  private def trySlice(implicit src: Array[Byte], from: Int, to: Int) = {
    try {
      src.slice(from, to)
    }
    catch {
      case e: Exception =>
        src.slice(from, src.length)
    }
  }
}




