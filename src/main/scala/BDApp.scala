import akka.actor.ActorRef
import blocks.BDBlock
import scorex.core.api.http.ApiRoute
import scorex.core.app.Application
import scorex.core.network.message.MessageSpec
import scorex.core.settings.ScorexSettings
import transaction.{BDTransaction, Sha256PreimageProposition}

class BDApp(args: Seq[String]) extends Application {
  override type P = Sha256PreimageProposition
  override type TX = BDTransaction
  override type PMOD = BDBlock
  override type NVHT = this.type
  override implicit val settings: ScorexSettings = _
  override val apiRoutes: Seq[ApiRoute] = _
  override protected val additionalMessageSpecs: Seq[MessageSpec[_]] = _
  override type _1 = this.type
  override val nodeViewHolderRef: ActorRef = _
  override val nodeViewSynchronizer: ActorRef = _
  override val localInterface: ActorRef = _
  override val swaggerConfig: String = _
}


object BDApp {

  def main(args: Array[String]): Unit = new BDApp(args).run()
}