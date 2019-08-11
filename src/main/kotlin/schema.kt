import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import com.google.common.io.Resources

@Suppress("UnstableApiUsage")
val schema = SchemaParser
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
  .resolvers(Query(), Mutation())
  .build()
  .makeExecutableSchema()

@Suppress("unused") // GraphQL by reflection
private class Query : GraphQLQueryResolver {
  fun allPeople() = PeopleRepository.all()
  fun findById(id: Int) = PeopleRepository.findById(id)
}

@Suppress("unused") // GraphQL by reflection
private class Mutation : GraphQLMutationResolver {
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