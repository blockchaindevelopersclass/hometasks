package nodeViewHolder

import blocks.BDBlock
import scorex.core.serialization.Serializer
import scorex.core.{ModifierTypeId, NodeViewHolder, NodeViewModifier}
import transaction.{BDTransaction, Sha256PreimageProposition}

class BDNodeViewHolder extends NodeViewHolder[Sha256PreimageProposition, BDTransaction, BDBlock] {
  override type SI = BDSyncInfo
  override type HIS = BDBlockchain
  override type MS = BDState
  override type VL = this.type
  override type MP = this.type

  override def restoreState(): Option[(BDNodeViewHolder.this.type, BDNodeViewHolder.this.type, BDNodeViewHolder.this.type, BDNodeViewHolder.this.type)] = ???

  override protected def genesisState: (BDNodeViewHolder.this.type, BDNodeViewHolder.this.type, BDNodeViewHolder.this.type, BDNodeViewHolder.this.type) = ???

  override val modifierSerializers: Map[ModifierTypeId, Serializer[_ <: NodeViewModifier]] = _
  override val networkChunkSize: Int = _
}
