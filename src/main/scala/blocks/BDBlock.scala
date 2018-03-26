package blocks

import io.circe.Json
import scorex.core.block.Block
import scorex.core.block.Block.Version
import scorex.core.serialization.Serializer
import scorex.core.{ModifierId, ModifierTypeId}
import scorex.crypto.hash.{Blake2b256, Digest32}
import transaction.{BDTransaction, Sha256PreimageProposition}

import scala.util.Try

case class BDBlock(transactions: Seq[BDTransaction],
                   parentId: ModifierId,
                   currentTarget: Long,
                   nonce: Long,
                   version: Version,
                   timestamp: Long) extends Block[Sha256PreimageProposition, BDTransaction] {
  override type M = BDBlock

  override val modifierTypeId: ModifierTypeId = BDBlock.BDBlockModifierTypeId

  val hash: Digest32 = Blake2b256(bytes)

  override val id: ModifierId = ModifierId @@ hash

  override def json: Json = ???

  override def serializer: Serializer[BDBlock] = BDBlockSerializer
}

object BDBlock {

  val BDBlockModifierTypeId: ModifierTypeId = ModifierTypeId @@ 10.toByte

}

object BDBlockSerializer extends Serializer[BDBlock] {

  override def toBytes(obj: BDBlock): Array[Version] = ???

  override def parseBytes(bytes: Array[Version]): Try[BDBlock] = ???

}

