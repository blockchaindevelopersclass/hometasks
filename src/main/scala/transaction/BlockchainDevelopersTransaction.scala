package transaction

import com.google.common.primitives.{Bytes, Ints, Longs}
import io.circe.Json
import scorex.core.serialization.Serializer
import scorex.core.transaction.Transaction

import scala.util.Try

case class BlockchainDevelopersTransaction(inputs: IndexedSeq[OutputId],
                                           outputs: IndexedSeq[(Sha256PreimageProposition, Value)],
                                           signatures: IndexedSeq[Sha256PreimageProof]
                                          ) extends Transaction[Sha256PreimageProposition] {
  override type M = BlockchainDevelopersTransaction

  override val messageToSign: Array[Byte] = BCTransactionSerializer.messageToSign(this)

  override def serializer: Serializer[BlockchainDevelopersTransaction] = BCTransactionSerializer

  override def json: Json = ???
}

object BCTransactionSerializer extends Serializer[BlockchainDevelopersTransaction] {
  override def toBytes(obj: BlockchainDevelopersTransaction): Array[Byte] = ???

  override def parseBytes(bytes: Array[Byte]): Try[BlockchainDevelopersTransaction] = ???

  def messageToSign(obj: BlockchainDevelopersTransaction): Array[Byte] = {
    Bytes.concat(
      Ints.toByteArray(obj.inputs.length),
      scorex.core.utils.concatFixLengthBytes(obj.inputs),
      Ints.toByteArray(obj.outputs.length),
      scorex.core.utils.concatFixLengthBytes(obj.outputs.map(p => Bytes.concat(p._1.hash, Longs.toByteArray(p._2)))))
  }
}