package task1

import scorex.crypto.hash.CryptographicHash32

class PoWMiner[HF <: CryptographicHash32](hashFunction: HF) {

  def doWork(data: Array[Byte], difficulty: BigInt): ProvedData = ???

  def validateWork(d: ProvedData, difficulty: BigInt): Boolean = BigInt(hashFunction.hash(d.bytes)) < difficulty

}
