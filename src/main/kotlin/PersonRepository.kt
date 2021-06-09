import graphql.schema.DataFetchingEnvironment

object PersonRepository {
  var people: MutableList<Person> = mutableListOf()

  init {
    val rocket = PetsRepository.findById(1)
    val sherlock = PetsRepository.findById(2)

    val jessica = Person(3, "Jessica", emptyList())
    val kim = Person(2, "Kim", listOf(sherlock))
    val arnab = Person(1, "Arnab", listOf(sherlock, rocket))

    addFriends(jessica, arnab)

    people.add(arnab)
    people.add(jessica)
    people.add(kim)
  }

  fun addPerson(name: String): Person {
    val id = (people.maxByOrNull { it.id }?.id ?: 0) + 1
    val person = Person(id, name, emptyList())
    people.add(person)
    return person
  }
  fun addFriends(first: Person, second: Person): Boolean =
    first.addFriend(second) && second.addFriend(first)

  fun allPeople() = people.toList()

  // Note: this can be expensive, so look at the Person.kt class for a hint on how to batch calls
  fun findById(id: Int) = people.first { it.id == id }
    /*.also {
    println("this is an expensive call to find the person with id $id")
  }*/

  fun findByIds(ids: List<Int>) = ids.map { findById(it) }
}

fun DataFetchingEnvironment.getPersonRepository(): PersonRepository = PersonRepository
