package transaction

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.hash.Sha256
import scorex.testkit.generators.CoreGenerators

class Sha256PreimageProofTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with Generators {


  property("Sha256PreimageProof should be serialized and deserialized") {
    forAll(preimageProofGenerator) { proof =>
      val recovered = proof.serializer.parseBytes(proof.serializer.toBytes(proof)).get
      proof.serializer.toBytes(proof) shouldEqual proof.serializer.toBytes(recovered)
    }
  }

}