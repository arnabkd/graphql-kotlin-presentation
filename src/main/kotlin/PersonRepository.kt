object PersonRepository {
  var people: MutableList<Person> = mutableListOf()

  init {
    val Jessica = Person(3, "Jessica", emptyList())
    val Kim = Person(2, "Kim", emptyList())
    val Arnab = Person(1, "Arnab", listOf(Kim, Jessica))


    people.add(Arnab)
    people.add(Jessica)
    people.add(Kim)
  }

  fun addPerson(person: Person) = people.add(person)
  fun allPeople() = people
  fun findById(id: Int) = people.first { it.id == id }
}