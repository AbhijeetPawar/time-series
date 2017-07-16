import java.time.Instant

import Analysis.PriceRatio
import org.scalatest.FunSpec

class AnalyzerTest extends FunSpec {

  describe("Analyzer") {
    it("should create analyzer given priceRatio sample") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)

      val analyzer = Analyzer.create(priceRatio)

      assert(analyzer === Analyzer(Stream(), priceRatio))
    }

    it("should create next analyzer given next priceRatio sample") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.900)
      val analyzer = Analyzer(Stream(), priceRatio1)

      val nextAnalyzer = Analyzer.create(priceRatio2, analyzer)

      assert(nextAnalyzer === Analyzer(Stream(priceRatio1), priceRatio2))
    }

    it("should create next analyzer based on previous window") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.9)
      val priceRatio3 = PriceRatio(Instant.ofEpochSecond(660), 1.9)

      val analyzer = Analyzer.create(priceRatio3, Analyzer(Stream(priceRatio1), priceRatio2))

      assert(analyzer === Analyzer(Stream(priceRatio2, priceRatio1), priceRatio3))
    }
  }

  describe("Analysis") {
    val window = 60

    it("should analyze new price ratio when no previous analysis present") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)

      val analyzer = Analyzer(Stream(), priceRatio)

      assert(analyzer.analyze(window) === Analysis.Analysis(priceRatio, 1, 1.805, 1.805, 1.805))
    }

    it("should analyze new price ratio when previous analysis present in same time window") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.900)

      val analyzer = Analyzer(Stream(priceRatio1), priceRatio2)

      assert(analyzer.analyze(window) === Analysis.Analysis(priceRatio2, 2, 3.705, 1.805, 1.9))
    }

    it("should analyze new price ratio for given time window") {
      val priceRatio1 = PriceRatio(Instant.ofEpochSecond(600), 1.805)
      val priceRatio2 = PriceRatio(Instant.ofEpochSecond(601), 1.9)
      val priceRatio3 = PriceRatio(Instant.ofEpochSecond(660), 1.9)

      val analyzer = Analyzer(Stream(priceRatio2, priceRatio1), priceRatio3)

      assert(analyzer.analyze(window) === Analysis.Analysis(priceRatio3, 2, 3.8, 1.9, 1.9))
    }
  }
}
