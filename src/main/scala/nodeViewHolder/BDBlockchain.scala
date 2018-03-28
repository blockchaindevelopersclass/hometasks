package nodeViewHolder

import blocks.BDBlock
import mining.BDMiner
import scorex.core.ModifierId
import scorex.core.consensus.BlockChain.Score
import scorex.core.consensus.History.ProgressInfo
import scorex.core.consensus.{BlockChain, History, ModifierSemanticValidity}
import scorex.crypto.encode.Base58
import transaction.{BDTransaction, Sha256PreimageProposition}

import scala.util.Try

case class BDBlockchain(blocks: Map[Int, BDBlock],
                        reverseMap: Map[String, Int], isValid: Map[BDBlock, Boolean])
  extends BlockChain[Sha256PreimageProposition, BDTransaction, BDBlock, BDSyncInfo, BDBlockchain] {

  private def key(id: Array[Byte]): String = Base58.encode(id)

  override def height(): Int = blocks.keys.max

  override def heightOf(id: ModifierId): Option[Int] = reverseMap.get(key(id))

  override def blockAt(height: Int): Option[BDBlock] = blocks.get(height)

  override def children(blockId: ModifierId): Seq[BDBlock] = heightOf(blockId).map(_ + 1).flatMap(blockAt).toSeq

  // TODO this is simplified version
  override def score(block: BDBlock): Score = BigInt(heightOf(block).getOrElse(0))

  override def chainScore(): Score = score(lastBlock)

  override def append(block: BDBlock): Try[(BDBlockchain, ProgressInfo[BDBlock])] = Try {
    if (!BDMiner.correctWorkDone(block)) throw new Error(s"Incorrect target for ${block.encodedId}")
    //TODO forks are not supported in this implementation
    if (!(lastBlock.id sameElements block.parentId)) throw new Error(s"Incorrect parentId for ${block.encodedId}")
    val blockHeight = height() + 1
    val progressInfo = ProgressInfo(None, Seq.empty, Some(block), Seq.empty)
    log.info(s"Appended block ${block.encodedId} with height ${blockHeight}")
    (BDBlockchain(blocks + (blockHeight -> block), reverseMap + (block.encodedId -> blockHeight), isValid), progressInfo)
  }

  override def reportSemanticValidity(modifier: BDBlock,
                                      valid: Boolean,
                                      lastApplied: ModifierId): (BDBlockchain, History.ProgressInfo[BDBlock]) = {
    if (valid) (BDBlockchain(blocks, reverseMap, isValid + (modifier -> true)), ProgressInfo[BDBlock](None, Seq.empty, None, Seq.empty))
    else (BDBlockchain(blocks - reverseMap(modifier.encodedId), reverseMap - modifier.encodedId, isValid + (modifier -> false)), ProgressInfo[BDBlock](None, Seq.empty, None, Seq.empty))
  }

  override def modifierById(id: ModifierId): Option[BDBlock] = reverseMap.get(key(id)).flatMap(h => blocks.get(h))

  override def isSemanticallyValid(id: ModifierId): ModifierSemanticValidity.Value = reverseMap.get(key(id)) match {
    case Some(_) => ModifierSemanticValidity.Valid
    case _ => ModifierSemanticValidity.Unknown
  }

  override def syncInfo: BDSyncInfo = {
    BDSyncInfo(lastBlockIds(BDSyncInfo.idsSize))
  }

  override def compare(other: BDSyncInfo): History.HistoryComparisonResult.Value = {
    val theirIds = other.ids
    theirIds.reverse.find(id => contains(id)) match {
      case Some(common) =>
        val commonHeight = heightOf(common).get
        val theirTotalHeight = theirIds.indexWhere(_ sameElements common) + commonHeight
        val ourHeight = height()
        if (theirTotalHeight == ourHeight) {
          History.HistoryComparisonResult.Equal
        } else if (theirTotalHeight > ourHeight) {
          History.HistoryComparisonResult.Older
        } else {
          History.HistoryComparisonResult.Younger
        }
      case _ => History.HistoryComparisonResult.Unknown
    }
  }

  override type NVCT = BDBlockchain

}

object BDBlockchain {

  val GenesisBlock: BDBlock = BDBlock(Seq.empty,
    ModifierId @@ Array.fill(32)(0: Byte),
    BDMiner.MaxTarget,
    0,
    0: Byte,
    1517329800000L)

  val empty: BDBlockchain = BDBlockchain(Map(1 -> GenesisBlock), Map(GenesisBlock.encodedId -> 1), Map(GenesisBlock -> true))

}
