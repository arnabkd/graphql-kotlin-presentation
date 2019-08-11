import com.github.javafaker.Faker
import graphql.schema.DataFetchingEnvironment
import java.text.SimpleDateFormat
import java.util.concurrent.CompletableFuture

@Suppress("unused") // GraphQL by reflection
data class Person(
  val id: Int, // modified for simplicity
  val name: String,
  val petIds: List<Int> = emptyList()
) : Searchable {

  private val friendIds = mutableListOf<Int>()
  override fun matches(queryString: String) = name.contains(queryString)

  val dateOfBirth = SimpleDateFormat("yyyy-MM-dd")
    .format(Faker().date().birthday())
    .replace("(\\d\\d)(\\d\\d)$", "$1:$2")

  val description = Faker().lorem().paragraphs(2).joinToString("\n")

  fun friends(env: DataFetchingEnvironment): CompletableFuture<List<Person>> =
    env.getPersonDataLoader().loadMany(friendIds)

  fun pets(env: DataFetchingEnvironment): CompletableFuture<List<Pet>> =
    env.getPetDataLoader().loadMany(petIds)

  fun addFriend(other: Person) =
    if (friendIds.contains(other.id)) true
    else friendIds.add(other.id)

}
