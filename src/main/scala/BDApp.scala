import akka.actor.{ActorRef, Props}
import blocks.BDBlock
import nodeViewHolder.{BDNodeViewHolder, BDWallet}
import scorex.core.api.http.ApiRoute
import scorex.core.app.Application
import scorex.core.network.message.MessageSpec
import scorex.core.settings.ScorexSettings
import transaction.{BDTransaction, Sha256PreimageProposition}

class BDApp(args: Seq[String]) extends Application {
  override type P = Sha256PreimageProposition
  override type TX = BDTransaction
  override type PMOD = BDBlock
  override type NVHT = BDWallet
  override implicit val settings: ScorexSettings = ScorexSettings.read(None)
  override val apiRoutes: Seq[ApiRoute] = Seq.empty
  override protected val additionalMessageSpecs: Seq[MessageSpec[_]] = Seq.empty

  override val nodeViewHolderRef: ActorRef = system.actorOf(Props(new BDNodeViewHolder(settings, minerSettings, timeProvider)))

  override val nodeViewSynchronizer: ActorRef = _
  override val localInterface: ActorRef = _
  override val swaggerConfig: String = _
}


object BDApp {

  def main(args: Array[String]): Unit = new BDApp(args).run()
}