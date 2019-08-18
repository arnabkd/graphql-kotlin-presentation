@SuppressWarnings("unused") // GraphQL by reflection
data class Person(
   val id: Int, // modified for simplicity
   val name: String,
   val friends: List<Person>
 )