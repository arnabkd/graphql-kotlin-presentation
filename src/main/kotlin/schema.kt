import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import com.google.common.io.Resources

val schema = createExecutableSchema()

@Suppress("UnstableApiUsage")
fun createExecutableSchema()= SchemaParser
  .newParser()
  .schemaString(
    Resources.toString(
      Resources.getResource("schema.graphql"),
      Charsets.UTF_8
    )
  )
  .dictionary( // Currently required for sealed classes
    mapOf(
    "Cat" to Cat::class.java,
    "Dog" to Dog::class.java
    )
  )
  .resolvers(QueryResolver(), MutationResolver())
  .build()
  .makeExecutableSchema()

@Suppress("unused") // GraphQL by reflection
class QueryResolver : GraphQLQueryResolver {
  fun person(id: Int) = PersonRepository.findById(id)
  fun allPeople() = PersonRepository.allPeople()
  fun search(input: SearchInput): List<Any> =
    (PersonRepository.allPeople() + PetsRepository.allPets()).filter {
      it.matches(input.queryString)
    }
}

@Suppress("unused") // GraphQL by reflection
class MutationResolver : GraphQLMutationResolver {
  fun addPerson(input: PersonInput): Person = PersonRepository.addPerson(input.name)
  fun addFriends(input: AddFriendsInput): Boolean {
    val first = PersonRepository.findById(input.first)
    val second = PersonRepository.findById(input.second)

    return PersonRepository.addFriends(first, second)
  }
}
