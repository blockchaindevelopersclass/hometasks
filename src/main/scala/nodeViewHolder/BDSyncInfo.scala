package nodeViewHolder

import scorex.core.ModifierId
import scorex.core.consensus.History.ModifierIds
import scorex.core.consensus.SyncInfo
import scorex.core.network.message.SyncInfoMessageSpec
import scorex.core.serialization.Serializer

import scala.util.Try

case class BDSyncInfo(ids: Seq[ModifierId])  extends SyncInfo {
  override type M = BDSyncInfo

  override def startingPoints: ModifierIds = ???

  override def serializer: Serializer[BDSyncInfo] = ???
}

object BDSyncInfoSerializer extends Serializer[BDSyncInfo] {
  override def toBytes(obj: BDSyncInfo): Array[Byte] = ???

  override def parseBytes(bytes: Array[Byte]): Try[BDSyncInfo] = ???
}


object BDSyncInfo {
  val idsSize = 100
}

object BDSyncInfoMessageSpec extends SyncInfoMessageSpec[BDSyncInfo](BDSyncInfoSerializer.parseBytes)