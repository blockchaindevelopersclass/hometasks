package nodeViewHolder

import akka.actor.ActorRef
import blocks.BDBlock
import scorex.core.network.NodeViewSynchronizer
import scorex.core.settings.NetworkSettings
import scorex.core.utils.NetworkTimeProvider
import transaction.{BDTransaction, Sha256PreimageProposition}

class BDNodeViewSynchronizer(networkControllerRef: ActorRef,
                             nodeViewHolderRef: ActorRef,
                             localInterfaceRef: ActorRef,
                             syncInfoSpec: BDSyncInfoMessageSpec.type,
                             networkSettings: NetworkSettings,
                             timeProvider: NetworkTimeProvider) extends NodeViewSynchronizer[Sha256PreimageProposition,
  BDTransaction, BDSyncInfo, BDSyncInfoMessageSpec.type, BDBlock, BDBlockchain, BDMempool](
  networkControllerRef, nodeViewHolderRef, localInterfaceRef, BDSyncInfoMessageSpec, networkSettings, timeProvider) {

}
