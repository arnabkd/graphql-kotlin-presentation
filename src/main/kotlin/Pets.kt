sealed class Pet: Searchable {
  abstract val id: Int
  abstract val name: String
  abstract fun makeSound(): String
}

@Suppress("unused") // GraphQL by reflection
data class Dog(
  override val id: Int,
  override val name: String
): Pet() {
  override fun matches(queryString: String) = name.contains(queryString)

  override fun makeSound() = "bark"
  fun barkLoudly() = "WOOF WOOF"
}

@Suppress("unused") // GraphQL by reflection
data class Cat(
  override val id: Int,
  override val name: String
): Pet() {
  override fun matches(queryString: String) = name.contains(queryString)
  override fun makeSound() = "purr"
  fun beLazy() = ""
}