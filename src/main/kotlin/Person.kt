@Suppress("unused") // GraphQL by reflection
data class Person(var name: String, val friendIds: List<Int>, val id: Int) {
  fun addFriend(friend: Person) = copy(friendIds = friendIds + friend.id)
  fun friends() =
    PeopleRepository.all(friendIds)
}