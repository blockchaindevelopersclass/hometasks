package transaction

import scorex.core.serialization.Serializer
import scorex.core.transaction.box.proposition.ProofOfKnowledgeProposition
import scorex.crypto.hash.Digest32

import scala.util.Try

case class Sha256PreimageProposition(hash: Digest32) extends ProofOfKnowledgeProposition[Sha256Preimage] {
  override type M = Sha256PreimageProposition

  override def serializer: Serializer[Sha256PreimageProposition] = Sha256HashPreimagePropositionSerializer

  override def equals(obj: scala.Any): Boolean = obj match {
    case pre: Sha256PreimageProposition => pre.hash sameElements hash
    case _ => false
  }
}

object Sha256HashPreimagePropositionSerializer extends Serializer[Sha256PreimageProposition] {
  override def toBytes(obj: Sha256PreimageProposition): Array[Byte] = obj.hash

  override def parseBytes(bytes: Array[Byte]): Try[Sha256PreimageProposition] = Try {
    require(bytes.length == 32)
    Sha256PreimageProposition(Digest32 @@ bytes)
  }
}