object PersonRepository {
  var people: MutableList<Person> = mutableListOf()

  fun clear() = people.clear()

  fun addPerson(name: String): Person {
    val id = nextId()
    val person = Person(id, name, emptyList())
    people.add(person)
    return person
  }

  fun addPerson(person: Person) =
    if (people.find { it.id == person.id } != null)
      throw IllegalArgumentException("Already one person with that ID")
    else
      people.add(person)

  fun addFriends(first: Person, second: Person): Boolean =
    first.addFriend(second) && second.addFriend(first)

  fun allIds() = people.map { it.id }
  fun allPeople() = people.toList().also {
    println("Expensive call to get all people")
  }

  // Note: this can be expensive, so look at the Person.kt class for a hint on how to batch calls
  fun findById(id: Int) = people.first { it.id == id }

  fun findByIds(ids: List<Int>) = people.filter { it.id in ids }.also {
    println("An expensive call to find the people with id $ids")
  }

  fun nextId() = (people.maxByOrNull { it.id }?.id ?: 0) + 1
}
