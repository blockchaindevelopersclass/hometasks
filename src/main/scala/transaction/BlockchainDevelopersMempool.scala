package transaction

import scorex.core.ModifierId
import scorex.core.transaction.MemoryPool

import scala.util.Try

class BlockchainDevelopersMempool(poolTxs: Seq[BlockchainDevelopersTransaction] = Seq())
  extends MemoryPool[BlockchainDevelopersTransaction, BlockchainDevelopersMempool] {

  override def put(tx: BlockchainDevelopersTransaction): Try[BlockchainDevelopersMempool] = put(Seq(tx))

  override def put(txs: Iterable[BlockchainDevelopersTransaction]): Try[BlockchainDevelopersMempool] = Try {
    putWithoutCheck(txs)
  }

  override def putWithoutCheck(txs: Iterable[BlockchainDevelopersTransaction]): BlockchainDevelopersMempool = {
    val newTransactions = txs.filter(tx => !poolTxs.contains(tx)).take(BlockchainDevelopersMempool.Limit - poolTxs.size)
    new BlockchainDevelopersMempool(poolTxs ++ newTransactions)
  }

  override def remove(tx: BlockchainDevelopersTransaction): BlockchainDevelopersMempool = {
    new BlockchainDevelopersMempool(poolTxs.filter(poolTx => poolTx != tx))
  }

  override def filter(condition: BlockchainDevelopersTransaction => Boolean): BlockchainDevelopersMempool = {
    new BlockchainDevelopersMempool(poolTxs.filter(condition))
  }

  override def getById(id: ModifierId): Option[BlockchainDevelopersTransaction] = poolTxs.find(_.id sameElements id)

  override def contains(id: ModifierId): Boolean = poolTxs.exists(_.id sameElements id)

  override def getAll(ids: Seq[ModifierId]): Seq[BlockchainDevelopersTransaction] = poolTxs

  override def size: Int = poolTxs.size

  override def take(limit: Int): Iterable[BlockchainDevelopersTransaction] = poolTxs.take(limit)

  override type NVCT = BlockchainDevelopersMempool
}

object BlockchainDevelopersMempool {
  val Limit = 1000
}