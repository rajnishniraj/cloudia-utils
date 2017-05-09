package io

import java.io.File

import akka.util.ByteString

import scala.io.Source
import scala.math.ceil

/**
  * Created by marcin on 5/7/17.
  */
case class Chunk(id: Int, content: ByteString) extends Serializable


object Chunkifier {

  def apply(implicit chunkSize: Int, file: File): List[Chunk] = {
    val src:String = Source.fromFile(file).mkString
    val chunks = for (i <- 0 until chunksCount(file, chunkSize))
      yield Chunk(i, trySlice(src, i*chunkSize, (i+1)*chunkSize ))
    chunks.toList


  }

  def chunksCount(file: File, chunkSize:Int): Int =
    ceil(file.length.toDouble / chunkSize).toInt

  private def trySlice(implicit src: String, from: Int, to: Int) = {
    try {
      ByteString(src.slice(from, to))
    }
    catch {
      case _: java.lang.StringIndexOutOfBoundsException =>
        ByteString(src.slice(from, src.length))
    }

  }
}




