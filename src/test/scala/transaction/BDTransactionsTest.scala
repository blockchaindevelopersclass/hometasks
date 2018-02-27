package transaction

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}

class BDTransactionsTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with Generators {


  property("BDTransactions should be serialized and deserialized") {
    forAll(BDTransactionGenerator) { tx =>
      val recovered = tx.serializer.parseBytes(tx.serializer.toBytes(tx)).get
      tx.serializer.toBytes(tx) shouldEqual tx.serializer.toBytes(recovered)
    }
  }

}