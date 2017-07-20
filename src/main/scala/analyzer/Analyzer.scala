package analyzer

import analyzer.Analysis.{PriceRatio, PriceRatioAnalysis}

case class Analyzer(previousWindow: List[PriceRatio], priceRatio: PriceRatio) {
  def getAnalysis: PriceRatioAnalysis = {
    previousWindow
      .map(ratio => ratio.analysis())
      .foldLeft(priceRatio.analysis()) { (acc, next) =>
        PriceRatioAnalysis(acc.priceRatio, acc.n + 1, acc.rs + next.rs, Math.min(acc.minV, next.minV), Math.max(acc.maxV, next.maxV))
      }
  }

  private def create(nextSample: PriceRatio, window: Int) = {
    val ratios = (priceRatio :: previousWindow).takeWhile(ratio => windowed(window, nextSample, ratio))
    Analyzer(ratios, nextSample)
  }

  private def windowed(windowLength: Int, current: PriceRatio, previous: PriceRatio): Boolean = {
    val windowStart = current.timestamp.minusSeconds(windowLength)
    previous.timestamp.isAfter(windowStart)
  }
}

object Analyzer {

  def analyze(window: Int)(ratios: Iterator[PriceRatio]): Iterator[Analyzer] = {
    ratios
      .scanLeft(Option.empty: Option[Analyzer]) { (analyzer, pr) =>
        analyzer match {
          case None => Option(Analyzer(List(), pr))
          case Some(previousAnalyzer) => Option(previousAnalyzer.create(pr, window))
        }
      }
      .flatten
  }

}