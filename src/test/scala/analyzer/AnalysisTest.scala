package analyzer

import java.time.Instant

import analyzer.Analysis.{PriceRatio, PriceRatioAnalysis}
import org.scalatest.FunSpec

class AnalysisTest extends FunSpec {
  describe("PriceRatio") {
    it("should create identity analysis for a priceRatio") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(600), 1.805)

      assert(priceRatio.analysis() === PriceRatioAnalysis(priceRatio, 1, 1.805, 1.805, 1.805))
    }
  }

  describe("Analysis") {
    it("should format analysis") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(1355270609), 1.80215)

      val analysis = PriceRatioAnalysis(priceRatio, 1, 1.80215, 1.80215, 1.80215)

      assert(analysis.formatted === "1355270609  1.80215   1    1.80215   1.80215   1.80215")
    }

    it("should round up decimal precision to 5 digits") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(1355270609), 1.8021567)

      val analysis = PriceRatioAnalysis(priceRatio, 1, 1.80215078, 1.802156789, 1.8021567890)

      assert(analysis.formatted === "1355270609  1.80216   1    1.80215   1.80216   1.80216")
    }

    it("should trim tailing zeros") {
      val priceRatio = PriceRatio(Instant.ofEpochSecond(1355270609), 1.80200)

      val analysis = PriceRatioAnalysis(priceRatio, 1, 1.80200, 1.80200, 1.80200)

      assert(analysis.formatted === "1355270609  1.802     1    1.802     1.802     1.802")
    }
  }
}
