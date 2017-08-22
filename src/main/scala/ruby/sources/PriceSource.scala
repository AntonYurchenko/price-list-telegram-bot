package ruby.sources

import java.io.{File, FileWriter}
import java.nio.file.{Files, StandardCopyOption}

import scala.io.Source

/**
  * The object gives access to price list
  *
  * @author Anton Yurchenko
  */
object PriceSource {

  /**
    * The method select price list file with max time stamp from work directory and read it.
    * The result is partitioned sequence.
    *
    * @param dir       File path to file
    * @param partition Int count of rows for each sequence (100 by default)
    * @return Seq[ Seq[String] ]
    */
  def readActual(dir: File, partition: Int = 100): Seq[Seq[String]] = {
    val fileList = dir.listFiles().filter(_.isFile)
    if (fileList.isEmpty) Nil
    else {
      val lastTsFile = fileList.max
      val price = Source.fromFile(lastTsFile, "UTF-8").getLines().toSeq
      if (price.length <= 100) price :: Nil
      else price.grouped(100).toSeq
    }
  }

  /**
    * The method writes content to file
    *
    * @param file    File path to file
    * @param content String content for writing
    * @param append  (true by default)
    * @return Boolean
    */
  def write(file: File, content: String, append: Boolean = true): Boolean = try {
    val writer = new FileWriter(file, append)
    writer.write(content)
    writer.flush()
    writer.close()
    true
  } catch {
    case _: Exception => false
  }

  /**
    * The method moves file atomically
    *
    * @param file1 File path to file
    * @param file2 File path to destination file
    * @return Boolean
    */
  def move(file1: File, file2: File): Boolean = try {
    Files.move(file1.toPath, file2.toPath, StandardCopyOption.ATOMIC_MOVE)
    true
  } catch {
    case _: Exception => false
  }

}
