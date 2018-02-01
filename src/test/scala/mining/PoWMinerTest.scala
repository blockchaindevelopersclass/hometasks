package mining

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.hash.Blake2b256
import scorex.testkit.generators.CoreGenerators

class PoWMinerTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with CoreGenerators {

  property("should generate valid prof for S") {
    val miner = new PoWMiner(Blake2b256)
    forAll(smallInt, nonEmptyBytesGen) { (difficulty, data) =>
      val proved = miner.doWork(data, difficulty)
      proved.data shouldEqual data
      miner.validateWork(proved, difficulty) shouldBe true
    }
  }
}