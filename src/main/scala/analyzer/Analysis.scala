package analyzer

import java.time.Instant

import utils.Formatter.{formatDouble, formatRow}

object Analysis {
  type Ratio = Double

  case class PriceRatio(timestamp: Instant, ratio: Ratio) {
    def analysis(): PriceRatioAnalysis = {
      PriceRatioAnalysis(this, 1, ratio, ratio, ratio)
    }
  }

  case class PriceRatioAnalysis(priceRatio: PriceRatio, n: Int, rs: Ratio, minV: Ratio, maxV: Ratio) {
    def formatted: String = formatRow(
      priceRatio.timestamp.getEpochSecond.toString,
      formatDouble(priceRatio.ratio),
      n.toString, formatDouble(rs), formatDouble(minV), formatDouble(maxV)
    )
  }

}
