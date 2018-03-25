package nodeViewHolder

import scorex.core.ModifierId
import scorex.core.transaction.MemoryPool
import transaction.BDTransaction

import scala.util.Try

class BlockchainDevelopersMempool(poolTxs: Seq[BDTransaction] = Seq())
  extends MemoryPool[BDTransaction, BlockchainDevelopersMempool] {

  override def put(tx: BDTransaction): Try[BlockchainDevelopersMempool] = put(Seq(tx))

  override def put(txs: Iterable[BDTransaction]): Try[BlockchainDevelopersMempool] = Try {
    txs.foreach(tx => require(tx.inputs.length == tx.signatures.length))
    putWithoutCheck(txs)
  }

  override def putWithoutCheck(txs: Iterable[BDTransaction]): BlockchainDevelopersMempool = {
    val newTransactions = txs.filter(tx => !poolTxs.contains(tx)).take(BlockchainDevelopersMempool.Limit - poolTxs.size)
    new BlockchainDevelopersMempool(poolTxs ++ newTransactions)
  }

  override def remove(tx: BDTransaction): BlockchainDevelopersMempool = {
    new BlockchainDevelopersMempool(poolTxs.filter(poolTx => poolTx != tx))
  }

  override def filter(condition: BDTransaction => Boolean): BlockchainDevelopersMempool = {
    new BlockchainDevelopersMempool(poolTxs.filter(condition))
  }

  override def getById(id: ModifierId): Option[BDTransaction] = poolTxs.find(_.id sameElements id)

  override def contains(id: ModifierId): Boolean = poolTxs.exists(_.id sameElements id)

  override def getAll(ids: Seq[ModifierId]): Seq[BDTransaction] = poolTxs

  override def size: Int = poolTxs.size

  override def take(limit: Int): Iterable[BDTransaction] = poolTxs.take(limit)

  override type NVCT = BlockchainDevelopersMempool
}

object BlockchainDevelopersMempool {
  val Limit = 50
}