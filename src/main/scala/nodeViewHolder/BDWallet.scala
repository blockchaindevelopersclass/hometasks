package nodeViewHolder

import blocks.BDBlock
import com.google.common.primitives.Ints
import scorex.core.VersionTag
import scorex.core.transaction.box.Box
import scorex.core.transaction.wallet.{Wallet, WalletBox, WalletTransaction}
import scorex.core.utils.ScorexLogging
import transaction.{BDTransaction, Sha256Preimage, Sha256PreimageCompanion, Sha256PreimageProposition}

import scala.util.{Success, Try}

case class BDWallet(seed: Array[Byte], secrets: Set[Sha256Preimage])
  extends Wallet[Sha256PreimageProposition, BDTransaction, BDBlock, BDWallet] with ScorexLogging {

  override type S = Sha256Preimage
  override type PI = Sha256PreimageProposition

  override def generateNewSecret(): BDWallet = {
    val newSecret = Sha256PreimageCompanion.generateKeys(seed ++ Ints.toByteArray(secrets.size))._1
    BDWallet(seed, secrets + newSecret)
  }

  //TODO not supported in this implementation
  override def historyTransactions: Seq[WalletTransaction[Sha256PreimageProposition, BDTransaction]] = Seq.empty

  //TODO not supported in this implementation
  override def boxes(): Seq[WalletBox[Sha256PreimageProposition, _ <: Box[Sha256PreimageProposition]]] = Seq.empty

  override def publicKeys: Set[Sha256PreimageProposition] = secrets.map(_.publicImage)

  override def secretByPublicImage(publicImage: PI): Option[S] = secrets.find(_.publicImage == publicImage)

  //TODO not supported in this implementation
  override def scanOffchain(tx: BDTransaction): BDWallet = this

  //TODO not supported in this implementation
  override def scanOffchain(txs: Seq[BDTransaction]): BDWallet = this

  //TODO not supported in this implementation
  override def scanPersistent(modifier: BDBlock): BDWallet = this

  //TODO not supported in this implementation
  override def rollback(to: VersionTag): Try[BDWallet] = Success(this)

  override type NVCT = this.type
}
