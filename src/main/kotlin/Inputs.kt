data class PersonInput(
  val name: String
)

data class SearchInput(
  val queryString: String
)

data class AddFriendsInput(
  val first: Int,
  val second: Int
)
