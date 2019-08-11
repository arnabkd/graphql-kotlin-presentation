@Suppress("unused") // GraphQL by reflection
data class Person(
  val id: Int,
  var name: String,
  val friendIds: List<Int> = emptyList(),
  val petIds: List<Int> = emptyList()
) {
  fun addFriend(friend: Person) = copy(friendIds = friendIds + friend.id)
  fun addPet(petId: Int) = copy(petIds = petIds + petId)
  fun friends() =
    PeopleRepository.all(friendIds)
  fun pets() =
    PetRepository.all(petIds)
}