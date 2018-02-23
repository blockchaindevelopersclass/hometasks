package mining

import scorex.crypto.hash.CryptographicHash32

import scala.math.BigInt

class PoWMiner[HF <: CryptographicHash32](hashFunction: HF) {

  private val MaxTarget: BigInt = BigInt(1, Array.fill(32)((-1).toByte))

  def doWork(data: Array[Byte], difficulty: BigInt): ProvedData = {
    def loop(nonce: Int): ProvedData = {
      val d = ProvedData(data, nonce)
      if (validateWork(d, difficulty)) d
      else loop(nonce + 1)
    }

    loop(0)
  }

  def validateWork(data: ProvedData, difficulty: BigInt): Boolean = realDifficulty(data) >= difficulty

  private def realDifficulty(noncedData: ProvedData): BigInt =
    MaxTarget / BigInt(1, hashFunction.hash(noncedData.bytes))

}
