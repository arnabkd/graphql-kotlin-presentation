import graphql.schema.DataFetchingEnvironment

@Suppress("unused") // GraphQL by reflection
data class Person(
  val id: Int, // modified for simplicity
  val name: String,
  val petIds: List<Int> = emptyList()
) : Searchable {

  private val friendIds = mutableListOf<Int>()
  override fun matches(queryString: String) = name.contains(queryString)

  // TODO: for the reader, how can we scale this?
  fun friends(env: DataFetchingEnvironment): List<Person> =
    PersonRepository.findByIds(friendIds)

  // TODO: for the reader, how can we scale this?
  fun pets(): List<Pet> =
    PetsRepository.findByIds(petIds)

  fun addFriend(other: Person) =
    friendIds.add(other.id)

}
