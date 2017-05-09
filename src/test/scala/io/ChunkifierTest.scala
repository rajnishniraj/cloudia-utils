package io

import java.io.File

import org.scalatest.FunSuite

/**
  * Created by marcin on 5/9/17.
  */
class ChunkifierTest extends FunSuite {

  test("testApply") {
    println("kierwa")
    implicit val chunkSize:Int = 100
    val filename = ".gitignore"
    println(Chunkifier(chunkSize, new File(filename)))
  }

}
