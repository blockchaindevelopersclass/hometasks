package nodeViewHolder

import scorex.core.ModifierId
import scorex.core.transaction.MemoryPool
import transaction.BDTransaction

import scala.util.Try

case class BDMempool(poolTxs: Seq[BDTransaction] = Seq())
  extends MemoryPool[BDTransaction, BDMempool] {

  override def put(tx: BDTransaction): Try[BDMempool] = put(Seq(tx))

  override def put(txs: Iterable[BDTransaction]): Try[BDMempool] = Try {
    txs.foreach(tx => require(tx.inputs.length == tx.signatures.length))
    putWithoutCheck(txs)
  }

  override def putWithoutCheck(txs: Iterable[BDTransaction]): BDMempool = {
    val newTransactions = txs.filter(tx => !poolTxs.contains(tx)).take(BDMempool.Limit - poolTxs.size)
    new BDMempool(poolTxs ++ newTransactions)
  }

  override def remove(tx: BDTransaction): BDMempool = {
    new BDMempool(poolTxs.filter(poolTx => poolTx != tx))
  }

  override def filter(condition: BDTransaction => Boolean): BDMempool = {
    new BDMempool(poolTxs.filter(condition))
  }

  override def getById(id: ModifierId): Option[BDTransaction] = poolTxs.find(_.id sameElements id)

  override def contains(id: ModifierId): Boolean = poolTxs.exists(_.id sameElements id)

  override def getAll(ids: Seq[ModifierId]): Seq[BDTransaction] = poolTxs

  override def size: Int = poolTxs.size

  override def take(limit: Int): Iterable[BDTransaction] = poolTxs.take(limit)

  override type NVCT = BDMempool
}

object BDMempool {
  val Limit = 50

  val empty: BDMempool = BDMempool(Seq.empty)
}