package mining

import com.google.common.primitives.Ints
import scorex.core.serialization.{BytesSerializable, Serializer}

import scala.util.Try

case class ProvedData(data: Array[Byte], nonce: Int) extends BytesSerializable {
  override type M = ProvedData

  override def serializer: Serializer[ProvedData] = ProvedDataSerializer
}

object ProvedDataSerializer extends Serializer[ProvedData] {
  override def toBytes(obj: ProvedData): Array[Byte] = Ints.toByteArray(obj.nonce) ++ obj.bytes

  override def parseBytes(bytes: Array[Byte]): Try[ProvedData] = Try {
    ProvedData(bytes.drop(4), Ints.fromByteArray(bytes.take(4)))
  }
}