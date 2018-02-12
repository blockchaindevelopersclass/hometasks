package crypto

import scorex.crypto.signatures.Signature

object Curve25519SignatureForger {

  def forgeSignature(signature: Signature): Signature = {
    val modifier: BigInt = BigInt("7237005577332262213973186563042994240829374041602535252466099000494570602496") +
      BigInt("27742317777372353535851937790883648493")
    Signature @@ (signature.take(32) ++ (BigInt(signature.takeRight(32).reverse) + modifier).toByteArray.reverse)
  }

}
