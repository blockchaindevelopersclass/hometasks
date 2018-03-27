package crypto

import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.crypto.signatures.Curve25519
import scorex.testkit.generators.CoreGenerators

class SignatureForgerTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with CoreGenerators {

  property("should be able to forge signature") {
    forAll(nonEmptyBytesGen) { data =>
      val pair = Curve25519.createKeyPair(data)
      val correctSignature = Curve25519.sign(pair._1, data)
      Curve25519.verify(correctSignature, data, pair._2) shouldBe true

      val forgedSignature = Curve25519SignatureForger.forgeSignature(correctSignature)
      (forgedSignature sameElements correctSignature) shouldBe false
      Curve25519.verify(forgedSignature, data, pair._2) shouldBe true
    }
  }
}