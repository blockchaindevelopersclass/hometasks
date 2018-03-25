package transaction

import org.scalacheck.Gen
import scorex.crypto.hash.Digest32
import scorex.testkit.generators.CoreGenerators

trait Generators extends CoreGenerators {

  val preimageProofGenerator: Gen[Sha256PreimageProof] = nonEmptyBytesGen
    .map(b => Sha256PreimageProof(Digest32Preimage @@ b))

  val preimagePropositionGenerator: Gen[Sha256PreimageProposition] = nonEmptyBytesGen
    .map(b => Sha256PreimageProposition(Digest32 @@ b))

  val outputGen: Gen[(Sha256PreimageProposition, Value)] = for {
    p <- preimagePropositionGenerator
    value <- positiveLongGen
  } yield p -> Value @@ value

  val BDTransactionGenerator: Gen[BDTransaction] = for {
    inputs <- Gen.nonEmptyListOf(genBytesList(32)).map(b => OutputId @@ b)
    outputs <- Gen.nonEmptyListOf(outputGen)
    signatures <- Gen.listOfN(inputs.length, preimageProofGenerator)
  } yield BDTransaction(inputs.toIndexedSeq, outputs.toIndexedSeq, signatures.toIndexedSeq)


}
