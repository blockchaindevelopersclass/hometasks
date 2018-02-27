package transaction

import org.scalacheck.Gen
import scorex.testkit.generators.CoreGenerators

trait Generators extends CoreGenerators {

  val preimageProofGenerator: Gen[Sha256PreimageProof] = nonEmptyBytesGen
    .map(b => Sha256PreimageProof(Digest32Preimage @@ b))

}
