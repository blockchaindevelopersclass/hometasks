package transaction

import scorex.core.serialization.Serializer
import scorex.core.transaction.proof.ProofOfKnowledge

import scala.util.Try

case class Sha256PreimageProof(preimage: Digest32Preimage) extends ProofOfKnowledge[Sha256Preimage, Sha256PreimageProposition]{
  override type M = Sha256PreimageProof

  override def isValid(proposition: Sha256PreimageProposition, message: Array[Byte]): Boolean = ???

  override def serializer: Serializer[Sha256PreimageProof] = Sha256PreimageProofSerializer
}

object Sha256PreimageProofSerializer extends Serializer[Sha256PreimageProof] {
  override def toBytes(obj: Sha256PreimageProof): Array[Byte] = ???

  override def parseBytes(bytes: Array[Byte]): Try[Sha256PreimageProof] = ???
}

