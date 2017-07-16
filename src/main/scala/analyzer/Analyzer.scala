package analyzer

import analyzer.Analysis.{PriceRatio, PriceRatioAnalysis}

case class Analyzer (previousWindow: Stream[PriceRatio], priceRatio: PriceRatio) {
  def getAnalysis(windowLength: Int): PriceRatioAnalysis = {
    previousWindow
      .takeWhile(ratio => windowed(windowLength, priceRatio, ratio))
      .map(ratio => ratio.analysis())
      .foldLeft(priceRatio.analysis()) { (acc, next) =>
        PriceRatioAnalysis(acc.priceRatio, acc.n + 1, acc.rs + next.rs, Math.min(acc.minV, next.minV), Math.max(acc.maxV, next.maxV))
      }
  }

  private def create(nextSample: PriceRatio) = {
    Analyzer(priceRatio #:: previousWindow, nextSample)
  }

  private def windowed(windowLength: Int, current: PriceRatio, previous: PriceRatio): Boolean = {
    val windowStart = current.timestamp.minusSeconds(windowLength)
    previous.timestamp.isAfter(windowStart)
  }
}

object Analyzer {

  def analyze(ratios: Iterator[PriceRatio]): Iterator[Analyzer] = {
    ratios
      .scanLeft(Option.empty: Option[Analyzer]) { (analyzer, pr) =>
        analyzer match {
          case None => Option(Analyzer(Stream(), pr))
          case Some(previousAnalyzer) => Option(previousAnalyzer.create(pr))
        }
      }
      .flatten
  }

}