package ruby.sources

import java.io.{File, FileWriter}
import java.nio.file.{Files, StandardCopyOption}

import scala.io.Source

object PriceSource {

  def readActual(dir: File, partition: Int = 100): Seq[Seq[String]] = {
    val fileList = dir.listFiles()
    if (fileList.isEmpty) Nil
    else {
      val lastTsFile = fileList.filter(_.isFile).max
      val price = Source.fromFile(lastTsFile, "UTF-8").getLines().toSeq
      if (price.length <= 100) price :: Nil
      else price.grouped(100).toSeq
    }
  }

  def write(file: File, content: String, append: Boolean = true): Boolean = try {
    val writer = new FileWriter(file, append)
    writer.write(content)
    writer.flush()
    writer.close()
    true
  } catch {
    case _: Exception => false
  }


  def move(file1: File, file2: File): Boolean = try {
    Files.move(file1.toPath, file2.toPath, StandardCopyOption.ATOMIC_MOVE)
    true
  } catch {
    case _: Exception => false
  }

}
