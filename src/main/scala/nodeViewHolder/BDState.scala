package nodeViewHolder

import blocks.BDBlock
import scorex.core.VersionTag
import scorex.core.transaction.state.MinimalState

import scala.util.{Failure, Try}

case class BDState(override val version: VersionTag) extends MinimalState[BDBlock, BDState] {
  override def applyModifier(mod: BDBlock): Try[BDState] = Try {
    BDState(VersionTag @@ mod.id)
  }

  override def rollbackTo(version: VersionTag): Try[BDState] = Failure(new Error("Not supported"))

  override def maxRollbackDepth: Int = 0

  override type NVCT = this.type
}


object BDState {

  val empty: BDState = BDState(VersionTag @@ BDBlockchain.GenesisBlock.id)
}