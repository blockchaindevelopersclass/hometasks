package nodeViewHolder

import scorex.core.consensus.History.ModifierIds
import scorex.core.consensus.SyncInfo
import scorex.core.serialization.Serializer

class DBSyncInfo  extends SyncInfo {
  override type M = DBSyncInfo

  override def startingPoints: ModifierIds = ???

  override def serializer: Serializer[DBSyncInfo.this.type] = ???
}
