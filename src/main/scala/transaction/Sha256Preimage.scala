package transaction

import scorex.core.serialization.Serializer
import scorex.core.transaction.box.Box
import scorex.core.transaction.state.{Secret, SecretCompanion}
import scorex.crypto.hash.Sha256

import scala.util.{Success, Try}

case class Sha256Preimage(preimage: Digest32Preimage) extends Secret {
  override type S = Sha256Preimage
  override type PK = Sha256PreimageProposition
  override type M = S

  override val companion: SecretCompanion[Sha256Preimage] = Sha256PreimageCompanion

  override val publicImage: Sha256PreimageProposition = Sha256PreimageProposition(Sha256(preimage))

  override val serializer: Serializer[Sha256Preimage] = Sha256PreimageSerializer
}

object Sha256PreimageSerializer extends Serializer[Sha256Preimage] {

  override def toBytes(obj: Sha256Preimage): Array[Byte] = obj.preimage

  override def parseBytes(bytes: Array[Byte]): Try[Sha256Preimage] = Success(Sha256Preimage(Digest32Preimage @@ bytes))

}

object Sha256PreimageCompanion extends SecretCompanion[Sha256Preimage] {
  override type PR = Sha256PreimageProof

  override def owns(secret: Sha256Preimage, box: Box[_]): Boolean = box.proposition match {
    case p: Sha256PreimageProposition => p.hash sameElements secret.publicImage.hash
    case _ => false
  }

  override def sign(secret: Sha256Preimage, message: Array[Byte]): Sha256PreimageProof = {
    Sha256PreimageProof(secret.preimage)
  }

  override def verify(message: Array[Byte], publicImage: Sha256PreimageProposition, proof: Sha256PreimageProof): Boolean = {
    proof.isValid(publicImage, message)
  }

  override def generateKeys(randomSeed: Array[Byte]): (Sha256Preimage, Sha256PreimageProposition) = {
    Sha256Preimage(Digest32Preimage @@ randomSeed) -> Sha256PreimageProposition(Sha256(randomSeed))
  }
}

