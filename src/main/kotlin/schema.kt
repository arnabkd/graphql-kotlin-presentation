import com.github.javafaker.Faker
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.SchemaParser
import com.google.common.io.Resources
import kotlin.random.Random

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
  fun generateData(): List<Person> {
    val faker = Faker()
    val newPeople = (1..500).map {
      val fakePersonName = faker.name().fullName()
      val petId = PetsRepository.nextId()

      val pet = when(Random.nextInt(2)) {
        0 -> Dog(petId, faker.dog().name())
        else -> Cat(petId, faker.cat().name())
      }
      PetsRepository.addPet(pet)

      val person = Person(PersonRepository.nextId(), fakePersonName, listOf(pet.id))
      PersonRepository.addPerson(person)

      person
    }

    val allPeople = PersonRepository.allPeople()

    newPeople.forEach {
      PersonRepository.addFriends(it, allPeople.random())
    }

    return newPeople
  }
  fun addPerson(input: PersonInput): Person = PersonRepository.addPerson(input.name)
  fun addFriends(input: AddFriendsInput): Boolean {
    val first = PersonRepository.findById(input.first)
    val second = PersonRepository.findById(input.second)

    return PersonRepository.addFriends(first, second)
  }
}
