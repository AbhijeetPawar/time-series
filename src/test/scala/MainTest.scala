import java.io.ByteArrayOutputStream

import org.scalatest.FunSuite

import scala.io.Source

class MainTest extends FunSuite {
  val outStream: ByteArrayOutputStream = new ByteArrayOutputStream()

  test("Integration test") {
    Console.withOut(outStream) {
      val filePath = getClass.getResource("input.txt").getPath
      val window = "60"
      Main.main(Array(filePath, window))
    }

    val expected = Source.fromResource("output.txt").getLines

    assert(outStream.toString().trim === expected.mkString("\n"))
  }
}
