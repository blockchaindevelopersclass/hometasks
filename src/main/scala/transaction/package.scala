import supertagged.TaggedType

package object transaction {

  object Digest32Preimage extends TaggedType[Array[Byte]]

  type Digest32Preimage = Digest32Preimage.Type

}
