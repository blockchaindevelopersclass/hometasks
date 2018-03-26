package transaction

import nodeViewHolder.BDMempool
import org.scalacheck.Gen
import org.scalatest.prop.{GeneratorDrivenPropertyChecks, PropertyChecks}
import org.scalatest.{Matchers, PropSpec}
import scorex.core.ModifierId
import scorex.testkit.generators.CoreGenerators
import scorex.testkit.properties.mempool.MempoolTransactionsTest
import utils.Generators

class BDMempoolTest extends PropSpec
  with PropertyChecks
  with GeneratorDrivenPropertyChecks
  with Matchers
  with CoreGenerators
  with MempoolTransactionsTest[Sha256PreimageProposition, BDTransaction, BDMempool]
  with Generators {

  override val memPool: BDMempool = new BDMempool

  override val transactionGenerator: Gen[BDTransaction] = BDTransactionGenerator

  property("size of mempool should increase when adding a single tx") {
    forAll(transactionGenerator) { tx =>
      val mp = memPool.put(tx)
      mp.isSuccess shouldBe true
      mp.get.size shouldEqual 1
    }
  }

  property("size of mempool should increase when adding a collection of txs") {
    forAll(transactionGenerator, transactionGenerator) { (tx1, tx2) =>
      whenever(!(tx1.id sameElements tx2.id)) {
        val mp = memPool.put(Array[BDTransaction](tx1, tx2))
        mp.isSuccess shouldBe true
        mp.get.size shouldEqual 2
      }
    }
  }

  property("adding existing txs without check should be successful") {
    forAll(transactionGenerator) { tx =>
      var mp = memPool.put(tx)
      mp.isSuccess shouldBe true
      var mp2 = mp.get.putWithoutCheck(Array[BDTransaction](tx, tx))
      mp2.size shouldBe 1
    }
  }

  property("size of mempool should decrease when removing existing tx") {
    forAll(transactionGenerator) { tx =>
      val mp = memPool.put(tx)
      mp.isSuccess shouldBe true
      val mp2 = mp.get.remove(tx)
      mp2.size shouldBe 0
    }
  }

  property("size of mempool should not decrease when removing non-existing tx") {
    forAll(transactionGenerator, transactionGenerator) { (tx1, tx2) =>
      whenever(!(tx1.id sameElements tx2.id)) {
        val mp = memPool.put(tx1)
        mp.isSuccess shouldBe true
        val mp2 = mp.get.remove(tx2)
        mp2.size shouldBe 1
      }
    }
  }

  property("mempool txs should be filtered") {
    forAll(transactionGenerator, transactionGenerator) { (tx1, tx2) =>
      whenever(!(tx1.id sameElements tx2.id)) {
        val mp = memPool.put(Array[BDTransaction](tx1, tx2))
        mp.isSuccess shouldBe true
        val mp2 = mp.get.filter(tx => tx.id equals tx1.id)
        mp2.size shouldBe 1
      }
    }
  }

  property("existing tx should be obtained by id") {
    forAll(transactionGenerator) { tx =>
      val mp = memPool.put(tx)
      mp.isSuccess shouldBe true
      val getTx = mp.get.getById(tx.id)
      getTx.isEmpty shouldBe false
    }
  }

  property("non-existing tx shouldn't be obtained by id") {
    forAll(transactionGenerator, transactionGenerator) { (tx1, tx2) =>
      whenever(!(tx1.id sameElements tx2.id)) {
        val mp = memPool.put(tx1)
        mp.isSuccess shouldBe true
        val getTx = mp.get.getById(tx2.id)
        getTx.isEmpty shouldBe true
      }
    }
  }

  property("mempool should contain existing tx") {
    forAll(transactionGenerator) { tx =>
      val mp = memPool.put(tx)
      mp.isSuccess shouldBe true
      mp.get.contains(tx.id) shouldBe true
    }
  }

  property("mempool shouldn't contain non-existing tx") {
    forAll(transactionGenerator, transactionGenerator) { (tx1, tx2) =>
      whenever(!(tx1.id sameElements tx2.id)) {
        val mp = memPool.put(tx1)
        mp.isSuccess shouldBe true
        mp.get.contains(tx2.id) shouldBe false
      }
    }
  }

  property("existing txs should be obtained by getAll") {
    forAll(transactionGenerator, transactionGenerator, transactionGenerator) { (tx1, tx2, tx3) =>
      whenever(!(tx1.id sameElements tx2.id) && !(tx1.id sameElements tx3.id) && !(tx3.id sameElements tx2.id)) {
        val allTxs = Array[BDTransaction](tx1, tx2, tx3)
        val selectedTxs = Array[BDTransaction](tx1, tx2)
        val selectedIds = Array[ModifierId](tx1.id, tx2.id)
        val mp = memPool.put(allTxs)
        mp.isSuccess shouldBe true
        mp.get.getAll(selectedIds) sameElements selectedTxs
      }
    }
  }

  property("required number of txs should be taken") {
    forAll(transactionGenerator, transactionGenerator, transactionGenerator) { (tx1, tx2, tx3) =>
      whenever(!(tx1.id sameElements tx2.id) && !(tx1.id sameElements tx3.id) && !(tx3.id sameElements tx2.id)) {
        val allTxs = Array[BDTransaction](tx1, tx2, tx3)
        val mp = memPool.put(allTxs)
        mp.isSuccess shouldBe true
        mp.get.take(2).size shouldBe 2
        mp.get.take(4).size shouldBe 3
      }
    }
  }

  property("mempool size should be limited") {
    val txs: Seq[BDTransaction] = (0 until BDMempool.Limit)
      .map(_ => transactionGenerator.sample.get)
    var pool: BDMempool = txs.foldLeft(memPool) { (currentPool, tx) =>
      currentPool.put(tx).get
    }
    pool.size shouldBe BDMempool.Limit

    forAll(transactionGenerator) { tx =>
      pool = pool.put(tx).get
      pool.size shouldBe BDMempool.Limit
    }
  }

  property("mempool should not add invalid transactions") {
    var pool = memPool
    val initialSize = pool.size
    forAll(transactionGenerator) { validTx =>
      val tx = validTx.copy(signatures = validTx.signatures.tail)
      pool.put(tx).isSuccess shouldBe false
      pool.size shouldBe initialSize
    }
  }

}