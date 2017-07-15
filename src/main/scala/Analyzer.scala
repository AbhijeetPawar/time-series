import Analysis.{Analysis, PriceRatio}

sealed trait Analyzer {
  def create(next: PriceRatio): Analyzer

  def analyze(): Analysis
}

case object EmptyAnalyzer extends Analyzer {
  override def create(next: PriceRatio): Analyzer = CurrentAnalyzer(List(), next)

  override def analyze(): Analysis = throw new UnsupportedOperationException("Cannot Analyse EmptyAnalyzer")
}

case class CurrentAnalyzer(window: List[PriceRatio], pr: PriceRatio) extends Analyzer {
  override def create(next: PriceRatio): Analyzer = {
    val nextWindow = (pr :: window).filter(p => windowed(next, p))

    CurrentAnalyzer(nextWindow, next)
  }

  override def analyze(): Analysis = {
    window
      .filter(p => windowed(pr, p))
      .map(r => r.analysis())
      .foldLeft(pr.analysis()) { (acc, next) =>
        Analysis.Analysis(acc.priceRatio, acc.n + 1, acc.rs + next.rs, Math.min(acc.minV, next.minV), Math.max(acc.maxV, next.maxV))
      }
  }

  private def windowed(next: PriceRatio, p: PriceRatio) = {
    next.timestamp.minusSeconds(60).isBefore(p.timestamp)
  }
}