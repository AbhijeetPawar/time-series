import java.time.Instant

import analyzer.Analysis.PriceRatio
import analyzer.Analyzer
import utils.Formatter

import scala.io.Source

object Main {

  def main(args: Array[String]): Unit = {
    val Array(filePath, window, _*) = args

    val runner = parse _ andThen Analyzer.analyze andThen evaluate(window.toInt)

    printHeader()
    runner(filePath)
  }

  private def parse(filePath: String): Iterator[PriceRatio] = {
    Source.fromFile(filePath)
      .getLines()
      .map(line => line.split("\\s+"))
      .map { case Array(ts, ratio, _*) => PriceRatio(Instant.ofEpochSecond(ts.toInt), ratio.toDouble) }
  }

  private def evaluate(window: Int)(analyzers: Iterator[Analyzer]): Unit = {
    analyzers
      .map(analyzer => analyzer.getAnalysis(window))
      .foreach(analysis => println(analysis.formatted))
  }

  private def printHeader(): Unit = {
    println(Formatter.formatRow("T", "V", "N", "RS", "MinV", "MaxV"))
    println("------------------------------------------------------------")
  }

}
