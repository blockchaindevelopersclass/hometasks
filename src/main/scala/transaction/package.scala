import supertagged.TaggedType

package object transaction {

  object Digest32Preimage extends TaggedType[Array[Byte]]

  type Digest32Preimage = Digest32Preimage.Type

  object OutputId extends TaggedType[Array[Byte]]

  type OutputId = OutputId.Type

  object Value extends TaggedType[Long]

  type Value = Value.Type


}
