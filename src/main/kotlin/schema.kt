import com.github.javafaker.Faker
import com.google.common.io.Resources
import graphql.kickstart.tools.GraphQLMutationResolver
import graphql.kickstart.tools.GraphQLQueryResolver
import graphql.kickstart.tools.SchemaParser
import graphql.scalars.ExtendedScalars
import kotlin.random.Random

val schema = createExecutableSchema()

@Suppress("UnstableApiUsage")
fun createExecutableSchema() = SchemaParser
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
  .scalars(ExtendedScalars.Date)
  .resolvers(
    QueryResolver(),
    MutationResolver().also { it.generateData() } // temporary
  )
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
  fun wipeData() = run {
    try {
      PersonRepository.clear()
      PetsRepository.clear()
      WipeDataPayload.SUCCESS
    } catch (e: Exception) {
      WipeDataPayload.FAILURE
    }
  }

  fun generateData(): List<Person> {
    val faker = Faker()
    val newPeople = (1..10).map {
      val fakePersonName = faker.name().nameWithMiddle()

      val petIds = (0..Random.nextInt(5)).map {
        val petId = PetsRepository.nextId()
        val pet = when (Random.nextInt(2)) {
          0 -> Dog(petId, faker.dog().name())
          else -> Cat(petId, faker.cat().name())
        }
        PetsRepository.addPet(pet)
        pet.id
      }

      val person = Person(
        PersonRepository.nextId(),
        fakePersonName,
        petIds
      )
      PersonRepository.addPerson(person)

      person
    }

    val allPeople = PersonRepository.allPeople()

    newPeople.forEach { newPerson ->
      val anotherPerson = allPeople.minus(newPerson).random()
      PersonRepository.addFriends(newPerson, anotherPerson)
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
