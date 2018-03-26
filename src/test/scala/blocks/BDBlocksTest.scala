package blocks

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import utils.Generators

class BDBlocksTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with Generators {


  property("BDBlockGenerator should be serialized and deserialized") {
    forAll(BDBlockGenerator) { block =>
      val recovered = block.serializer.parseBytes(block.serializer.toBytes(block)).get
      block.serializer.toBytes(block) shouldEqual block.serializer.toBytes(recovered)
    }
  }

}