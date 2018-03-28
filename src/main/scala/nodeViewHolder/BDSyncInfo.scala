package nodeViewHolder

import blocks.BDBlock
import scorex.core.ModifierId
import scorex.core.consensus.History.ModifierIds
import scorex.core.consensus.SyncInfo
import scorex.core.network.message.SyncInfoMessageSpec
import scorex.core.serialization.Serializer

import scala.util.Try

case class BDSyncInfo(ids: Seq[ModifierId]) extends SyncInfo {
  override type M = BDSyncInfo

  override val startingPoints: ModifierIds = Seq((BDBlock.BDBlockModifierTypeId, ids.head))

  override def serializer: Serializer[BDSyncInfo] = BDSyncInfoSerializer
}

object BDSyncInfoSerializer extends Serializer[BDSyncInfo] {
  override def toBytes(obj: BDSyncInfo): Array[Byte] = scorex.core.utils.concatFixLengthBytes(obj.ids)

  override def parseBytes(bytesIn: Array[Byte]): Try[BDSyncInfo] = Try {
    def loop(bytes: Array[Byte], acc: Seq[ModifierId]): BDSyncInfo = if (bytes.nonEmpty) {
      val nextId: ModifierId = ModifierId @@ bytes.take(32)
      loop(bytes.drop(32), acc ++ Seq(nextId))
    } else {
      BDSyncInfo(acc)
    }
    loop(bytesIn, Seq.empty)
  }
}


object BDSyncInfo {
  val idsSize = 100
}

object BDSyncInfoMessageSpec extends SyncInfoMessageSpec[BDSyncInfo](BDSyncInfoSerializer.parseBytes)