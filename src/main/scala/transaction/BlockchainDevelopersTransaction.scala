package transaction

import io.circe.Json
import scorex.core.serialization.Serializer
import scorex.core.transaction.Transaction

import scala.util.Try

case class BlockchainDevelopersTransaction(inputs: IndexedSeq[OutputId],
                                           outputs: IndexedSeq[(Sha256PreimageProposition, Value)],
                                           signatures: IndexedSeq[Sha256PreimageProof]
                        ) extends Transaction[Sha256PreimageProposition] {
  override type M = BlockchainDevelopersTransaction

  override val messageToSign: Array[Byte] = ???

  override def serializer: Serializer[BlockchainDevelopersTransaction] = BCTransactionSerializer

  override def json: Json = ???
}

object BCTransactionSerializer extends Serializer[BlockchainDevelopersTransaction] {
  override def toBytes(obj: BlockchainDevelopersTransaction): Array[Byte] = ???

  override def parseBytes(bytes: Array[Byte]): Try[BlockchainDevelopersTransaction] = ???
}