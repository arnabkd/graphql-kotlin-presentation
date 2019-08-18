data class PersonInput(
  val name: String
) {
  val person by lazy {
    val id = PeopleRepository.getNextId()
    Person(id, name, emptyList(), emptyList())
  }
}