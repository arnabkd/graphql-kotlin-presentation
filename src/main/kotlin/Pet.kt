@Suppress("unused") // GraphQL by reflection
sealed class Pet {
  abstract val id: Int
  abstract val name: String
  abstract fun makeSound(): String

  data class Dog(
    override val id: Int,
    override val name: String
  ): Pet() {
    override fun makeSound() = "$name barks"
    fun wagTail() = "$name wags his tail"
  }

  data class Cat(
    override val id: Int,
    override val name: String
  ): Pet() {
    override fun makeSound() = "$name meows"
    fun purr() = "$name purrs"
  }
}
