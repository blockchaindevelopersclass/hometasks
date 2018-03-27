package serialization

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import utils.Generators

class SerializationTests extends PropSpec
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

  property("BDTransactions should be serialized and deserialized") {
    forAll(BDTransactionGenerator) { tx =>
      val recovered = tx.serializer.parseBytes(tx.serializer.toBytes(tx)).get
      tx.serializer.toBytes(tx) shouldEqual tx.serializer.toBytes(recovered)
    }
  }

  property("BDSyncInfoGen should be serialized and deserialized") {
    forAll(BDSyncInfoGen) { info =>
      val recovered = info.serializer.parseBytes(info.serializer.toBytes(info)).get
      info.serializer.toBytes(info) shouldEqual info.serializer.toBytes(recovered)
    }
  }


}