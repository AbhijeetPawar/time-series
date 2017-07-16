package utils

import org.scalatest.FunSpec

class FormatterTest extends FunSpec {

  describe("formatDouble") {
    it("should round double to 5 digits") {
      assert(Formatter.formatDouble(1.234567) === "1.23457")
    }

    it("should trim tailing zeros") {
      assert(Formatter.formatDouble(1.2340) === "1.234")
    }
  }

  describe("leftpad") {
    it("should add space as padding") {
      assert(Formatter.leftPad(6, "1.23") === "1.23  ")
    }

    it("should not trim on overflow") {
      assert(Formatter.leftPad(4, "1.2345") === "1.2345")
    }
  }

  describe("formatRow") {
    it("should format output row with padding") {
      assert(Formatter.formatRow("T", "V", "N", "RS", "MinV", "MaxV") === "T           V         N    RS        MinV      MaxV")
    }
  }

}
