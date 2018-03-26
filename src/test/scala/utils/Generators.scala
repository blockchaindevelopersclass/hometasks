package utils

import blocks.BDBlock
import org.scalacheck.{Arbitrary, Gen}
import scorex.core.ModifierId
import scorex.core.block.Block.Version
import scorex.crypto.hash.Digest32
import scorex.testkit.generators.CoreGenerators
import transaction._

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

  val BDBlockGenerator: Gen[BDBlock] = for {
    parentId <- genBytesList(32).map(b => ModifierId @@ b)
    transactions <- smallInt.flatMap(n => Gen.listOfN(n, BDTransactionGenerator))
    currentTarget <- positiveLongGen
    nonce <- positiveLongGen
    timestamp <- positiveLongGen
    version <- Arbitrary.arbitrary[Byte]
  } yield BDBlock(transactions: Seq[BDTransaction],
    parentId: ModifierId,
    currentTarget: Long,
    nonce: Long,
    version: Version,
    timestamp: Long)

}
