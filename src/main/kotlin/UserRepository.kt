object UserRepository {

  private val users = mapOf(
    "admin:admin" to User("admin", 1),
    "test:test" to User("test", 2)
  )

  fun getUser(username: String, password: String): User? = users["$username:$password"]
  data class User(val username: String, val userId: Int)
}
