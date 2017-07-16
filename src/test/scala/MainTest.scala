import java.io.{ByteArrayOutputStream, PrintStream}

import org.scalatest.{BeforeAndAfter, FunSuite}

import scala.io.Source

class MainTest extends FunSuite with BeforeAndAfter {
  val outStream: ByteArrayOutputStream = new ByteArrayOutputStream()

  test("Integration test") {
    Console.withOut(outStream) {
      val filePath = getClass.getResource("input.txt").getPath
      Main.main(Array(filePath, "60"))
    }

    val expected = Source.fromResource("output.txt").getLines

    assert(outStream.toString().trim === expected.mkString("\n"))
  }
}
