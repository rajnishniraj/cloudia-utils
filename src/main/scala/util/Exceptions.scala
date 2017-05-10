package util

import java.io.File

/**
  * Created by marcin on 5/5/17.
  */
case class NotADirectoryException(file: File) extends Exception
case class NotAFileException(file: File) extends Exception
case class ChunkNotFoundException(chunkId: Int) extends Exception

