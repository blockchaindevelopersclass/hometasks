package transaction

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.hash.Sha256
import scorex.testkit.generators.CoreGenerators

class Sha256PreimageTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with CoreGenerators {

  property("Sha256PreimageCompanion should generate correct keys") {
    forAll(nonEmptyBytesGen) { seed =>
      val keys = Sha256PreimageCompanion.generateKeys(seed)
      keys._2 shouldEqual keys._1.publicImage
    }
  }

  property("Sha256PreimageCompanion should be able to verify prevoiusly signed messages") {
    forAll(nonEmptyBytesGen, nonEmptyBytesGen) { (seed, message) =>
      val keys = Sha256PreimageCompanion.generateKeys(seed)
      val proof = Sha256PreimageCompanion.sign(keys._1, message)
      Sha256PreimageCompanion.verify(message, keys._2, proof) shouldBe true
    }
  }

  property("Sha256PreimageCompanion modified proof should be incorrect") {
    forAll(nonEmptyBytesGen, nonEmptyBytesGen) { (seed, message) =>
      val keys = Sha256PreimageCompanion.generateKeys(seed)
      val proof = Sha256PreimageCompanion.sign(keys._1, message)
      Sha256PreimageCompanion.verify(message, keys._2, proof.copy(preimage = Digest32Preimage @@ proof.preimage.drop(1))) shouldBe false
    }
  }

  property("Sha256Preimage should be serialized and deserialized") {
    forAll(nonEmptyBytesGen) { seed =>
      val (secret, _) = Sha256PreimageCompanion.generateKeys(seed)
      val recovered = secret.serializer.parseBytes(secret.serializer.toBytes(secret)).get
      secret.serializer.toBytes(secret) shouldEqual secret.serializer.toBytes(recovered)
    }
  }

  property("Sha256PreimageProposition should be serialized and deserialized") {
    forAll(nonEmptyBytesGen) { seed =>
      val proposition = Sha256PreimageProposition(Sha256(seed))
      val recovered = proposition.serializer.parseBytes(proposition.serializer.toBytes(proposition)).get
      proposition.serializer.toBytes(proposition) shouldEqual proposition.serializer.toBytes(recovered)
    }
  }

}