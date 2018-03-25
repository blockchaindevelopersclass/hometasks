package nodeViewHolder

import scorex.core.ModifierId
import scorex.core.consensus.History.ModifierIds
import scorex.core.consensus.SyncInfo
import scorex.core.serialization.Serializer

case class BDSyncInfo(ids: Seq[ModifierId])  extends SyncInfo {
  override type M = BDSyncInfo

  override def startingPoints: ModifierIds = ???

  override def serializer: Serializer[BDSyncInfo.this.type] = ???
}


object BDSyncInfo {
  val idsSize = 100
}