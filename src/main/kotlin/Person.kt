import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletionStage

@Suppress("unused") // GraphQL by reflection
data class Person(
  val id: Int, // modified for simplicity
  val name: String,
  val pets: List<Pet> = emptyList()
) : Searchable {
  private val friendIds = mutableListOf<Int>()
  override fun matches(queryString: String) = name.contains(queryString)

  // TODO: left to the reader, figure out a way to use BatchLoaders.
  // See: https://www.graphql-java.com/documentation/v12/batching/
  fun friends(env: DataFetchingEnvironment) =
    env
      .getPersonRepository()
      .findByIds(friendIds)

  fun addFriend(other: Person) =
    friendIds.add(other.id)

}
