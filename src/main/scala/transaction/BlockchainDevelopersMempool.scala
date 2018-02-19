package transaction

import scorex.core.ModifierId
import scorex.core.transaction.MemoryPool

import scala.util.Try

class BlockchainDevelopersMempool extends MemoryPool[BlockchainDevelopersTransaction, BlockchainDevelopersMempool] {

  override def put(tx: BlockchainDevelopersTransaction): Try[BlockchainDevelopersMempool] = ???

  override def put(txs: Iterable[BlockchainDevelopersTransaction]): Try[BlockchainDevelopersMempool] = ???

  override def putWithoutCheck(txs: Iterable[BlockchainDevelopersTransaction]): BlockchainDevelopersMempool = ???

  override def remove(tx: BlockchainDevelopersTransaction): BlockchainDevelopersMempool = ???

  override def filter(condition: BlockchainDevelopersTransaction => Boolean): BlockchainDevelopersMempool = ???

  override def getById(id: ModifierId): Option[BlockchainDevelopersTransaction] = ???

  override def contains(id: ModifierId): Boolean = ???

  override def getAll(ids: Seq[ModifierId]): Seq[BlockchainDevelopersTransaction] = ???

  override def size: Int = ???

  override def take(limit: Int): Iterable[BlockchainDevelopersTransaction] = ???

  override type NVCT = BlockchainDevelopersMempool
}
