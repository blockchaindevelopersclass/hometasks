package nodeViewHolder

import akka.actor.{ActorRef, ActorSystem, Props}
import blocks.{BDBlock, BDBlockSerializer}
import scorex.core.serialization.Serializer
import scorex.core.settings.ScorexSettings
import scorex.core.transaction.Transaction
import scorex.core.utils.NetworkTimeProvider
import scorex.core.{ModifierTypeId, NodeViewHolder, NodeViewModifier}
import transaction.{BCTransactionSerializer, BDTransaction, Sha256PreimageProposition}

class BDNodeViewHolder(settings: ScorexSettings,
                       timeProvider: NetworkTimeProvider)
  extends NodeViewHolder[Sha256PreimageProposition, BDTransaction, BDBlock] {

  override type SI = BDSyncInfo
  override type HIS = BDBlockchain
  override type MS = BDState
  override type VL = BDWallet
  override type MP = BDMempool

  override def restoreState(): Option[(BDBlockchain, BDState, BDWallet, BDMempool)] = None

  override protected def genesisState: (BDBlockchain, BDState, BDWallet, BDMempool) =
    (BDBlockchain.empty, BDState.empty, BDWallet.empty, BDMempool.empty)

  override val modifierSerializers: Map[ModifierTypeId, Serializer[_ <: NodeViewModifier]] =
    Map(BDBlock.ModifierTypeId -> BDBlockSerializer,
      Transaction.ModifierTypeId -> BCTransactionSerializer)

  override val networkChunkSize: Int = settings.network.networkChunkSize
}


object BDNodeViewHolder {
  def props(settings: ScorexSettings,
            timeProvider: NetworkTimeProvider): Props =
    Props(new BDNodeViewHolder(settings, timeProvider))

  def apply(settings: ScorexSettings,
            timeProvider: NetworkTimeProvider)
           (implicit system: ActorSystem): ActorRef =
    system.actorOf(props(settings, timeProvider))

}
