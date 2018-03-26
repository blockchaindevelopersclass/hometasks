package nodeViewHolder

import akka.actor.{ActorRef, ActorSystem, Props}
import blocks.BDBlock
import scorex.core.{LocalInterface, ModifierId}
import transaction.{BDTransaction, Sha256PreimageProposition}

class BDLocalInterface(override val viewHolderRef: ActorRef)
  extends LocalInterface[Sha256PreimageProposition, BDTransaction, BDBlock] {
  override protected def onSuccessfulTransaction(tx: BDTransaction): Unit = {}

  override protected def onFailedTransaction(tx: BDTransaction): Unit = {}

  override protected def onStartingPersistentModifierApplication(pmod: BDBlock): Unit = {}

  override protected def onSyntacticallySuccessfulModification(mod: BDBlock): Unit = {}

  override protected def onSyntacticallyFailedModification(mod: BDBlock): Unit = {}

  override protected def onSemanticallySuccessfulModification(mod: BDBlock): Unit = {}

  override protected def onSemanticallyFailedModification(mod: BDBlock): Unit = {}

  override protected def onNewSurface(newSurface: Seq[ModifierId]): Unit = {}

  override protected def onRollbackFailed(): Unit = {}

  override protected def onNoBetterNeighbour(): Unit = {}

  override protected def onBetterNeighbourAppeared(): Unit = {}
}


object BDLocalInterfaceRef {

  def props(viewHolderRef: ActorRef): Props =
    Props(new BDLocalInterface(viewHolderRef))

  def apply(viewHolderRef: ActorRef)
           (implicit system: ActorSystem): ActorRef =
    system.actorOf(props(viewHolderRef))

}