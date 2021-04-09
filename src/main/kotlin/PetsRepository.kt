object PetsRepository {
  var pets = mutableListOf<Pet>()

  init {
    val rocket = Dog(1, "rocket")
    val sherlock = Cat(2, "sherlock")
    val max = Dog(3, "max")
    pets.add(rocket)
    pets.add(sherlock)
    pets.add(max)
  }

  fun findById(id: Int) = pets.first { it.id == id }
  fun allPets() = pets.toList()
}
