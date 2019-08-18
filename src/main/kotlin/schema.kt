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
  .dictionary(mapOf(
    "Dog" to Pet.Dog::class.java,
    "Cat" to Pet.Cat::class.java
  ))
  .resolvers(QueryResolver(), MutationResolver())
  .build()
  .makeExecutableSchema()

@Suppress("unused") // GraphQL by reflection
class QueryResolver : GraphQLQueryResolver {
  fun allPeople() = PeopleRepository.all()
  fun findById(id: Int) = PeopleRepository.findById(id)
}

@Suppress("unused") // GraphQL by reflection
class MutationResolver : GraphQLMutationResolver {
  fun addPerson(input: PersonInput) = PeopleRepository.addPerson(input.person)

  fun addFriend(firstFriendId: Int, secondFriendId: Int): Boolean {
    PeopleRepository.addFriendConnection(firstFriendId, secondFriendId)
    return true
  }

  fun addPet(personId: Int, petId: Int): Boolean {
    val owner = PeopleRepository.findById(personId)
    val pet = PetRepository.findById(petId)
    owner.addPet(pet.id)
    return true
  }
}