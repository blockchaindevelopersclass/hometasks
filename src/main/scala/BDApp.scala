import akka.actor.{ActorRef, Props}
import blocks.BDBlock
import mining.BDMiner.MineBlock
import mining.BDMinerRef
import nodeViewHolder._
import scorex.core.api.http.{ApiRoute, NodeViewApiRoute, PeersApiRoute, UtilsApiRoute}
import scorex.core.app.Application
import scorex.core.network.NodeViewSynchronizer
import scorex.core.network.message.MessageSpec
import scorex.core.settings.ScorexSettings
import transaction.{BDTransaction, Sha256PreimageProposition}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.Random
import scala.concurrent.duration._

class BDApp(args: Seq[String]) extends {
  override implicit val settings: ScorexSettings = ScorexSettings.read(args.headOption)
} with Application {
  override type P = Sha256PreimageProposition
  override type TX = BDTransaction
  override type PMOD = BDBlock
  override type NVHT = BDNodeViewHolder

  override protected lazy val additionalMessageSpecs: Seq[MessageSpec[_]] = Seq(BDSyncInfoMessageSpec)

  override val nodeViewHolderRef: ActorRef = BDNodeViewHolderRef(settings, timeProvider)

  override val localInterface: ActorRef = BDLocalInterfaceRef(nodeViewHolderRef)

  override val nodeViewSynchronizer: ActorRef =
    actorSystem.actorOf(Props(new BDNodeViewSynchronizer(networkControllerRef, nodeViewHolderRef, localInterface,
      BDSyncInfoMessageSpec, settings.network, timeProvider)))

  override val swaggerConfig: String = ""
  override val apiRoutes: Seq[ApiRoute] = Seq(
    UtilsApiRoute(settings.restApi),
    NodeViewApiRoute[P, TX](settings.restApi, nodeViewHolderRef),
    PeersApiRoute(peerManagerRef, networkControllerRef, settings.restApi)
  )

  if (settings.network.nodeName.contains("mining-node")) {
    val miner = BDMinerRef(nodeViewHolderRef, timeProvider)
    actorSystem.scheduler.scheduleOnce(10.second) {
      miner ! MineBlock(0)
    }
  }
}


object BDApp {

  def main(args: Array[String]): Unit = new BDApp(args).run()
}