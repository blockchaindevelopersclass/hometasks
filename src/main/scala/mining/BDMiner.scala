package mining

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import blocks.BDBlock
import mining.BDMiner.MineBlock
import nodeViewHolder.BDBlockchain
import scorex.core.LocalInterface.LocallyGeneratedModifier
import scorex.core.NodeViewHolder
import scorex.core.NodeViewHolder.{SemanticallySuccessfulModifier, Subscribe}
import scorex.core.utils.{NetworkTimeProvider, ScorexLogging}
import scorex.crypto.encode.Base58

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.math.BigInt
import scala.util.Random

class BDMiner(viewHolderRef: ActorRef, timeProvider: NetworkTimeProvider) extends Actor with ScorexLogging {

  override def preStart(): Unit = {
    val events = Seq(NodeViewHolder.EventType.SuccessfulSemanticallyValidModifier,
      NodeViewHolder.EventType.MempoolChanged)
    viewHolderRef ! Subscribe(events)
  }

  var currentCandidate: BDBlock = constructNewBlock(BDBlockchain.GenesisBlock)

  override def receive: Receive = {
    case SemanticallySuccessfulModifier(mod: BDBlock@unchecked) if mod.isInstanceOf[BDBlock] =>
      currentCandidate = constructNewBlock(mod)

    case MineBlock(newNonce) =>
      val newBlock = currentCandidate.copy(nonce = newNonce)
      if (BDMiner.correctWorkDone(newBlock)) {
        log.info(s"New block ${newBlock.encodedId} found")
        viewHolderRef ! LocallyGeneratedModifier(newBlock)
      }
      context.system.scheduler.scheduleOnce(10.second) {
        self ! MineBlock(Random.nextLong())
      }

    case _ => log.warn("Unexpeted message")
  }

  private def constructNewBlock(parent: BDBlock): BDBlock = {
    val transactions = Seq.empty
    val target = parent.currentTarget
    BDBlock(transactions,
      parent.id,
      target,
      0,
      0: Byte,
      timeProvider.time())
  }

}


object BDMiner {

  case class MineBlock(nonce: Long)

  val MaxTarget: Long = Long.MaxValue

  def correctWorkDone(block: BDBlock): Boolean = {
    realDifficulty(block) <= block.currentTarget
  }

  private def realDifficulty(block: BDBlock): BigInt = MaxTarget / BigInt(1, block.hash)

}

object BDMinerRef {
  def props(viewHolderRef: ActorRef, timeProvider: NetworkTimeProvider): Props =
    Props(new BDMiner(viewHolderRef: ActorRef, timeProvider: NetworkTimeProvider))

  def apply(viewHolderRef: ActorRef, timeProvider: NetworkTimeProvider)
           (implicit system: ActorSystem): ActorRef =
    system.actorOf(props(viewHolderRef, timeProvider))

}