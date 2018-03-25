package nodeViewHolder

import scorex.core.ModifierId
import scorex.core.consensus.History.ModifierIds
import scorex.core.consensus.SyncInfo
import scorex.core.serialization.Serializer

case class DBSyncInfo(ids: Seq[ModifierId])  extends SyncInfo {
  override type M = DBSyncInfo

  override def startingPoints: ModifierIds = ???

  override def serializer: Serializer[DBSyncInfo.this.type] = ???
}


object DBSyncInfo {
  val idsSize = 100
}