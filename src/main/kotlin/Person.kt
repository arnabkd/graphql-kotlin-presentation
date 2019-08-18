@Suppress("unused") // GraphQL by reflection
data class Person(
   val id: Int, // modified for simplicity
   val name: String,
   val friends: List<Person>,
   val pets: List<Pet> = emptyList()
 ): Searchable {
  override fun matches(queryString: String) = name.contains(queryString)
}