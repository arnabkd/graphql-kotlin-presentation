object PetsRepository {
  var pets = mutableListOf<Pet>()

  init {
    val dog = Dog(1, "rocket")
    val cat = Cat(2, "sherlock")
    pets.add(dog)
    pets.add(cat)
  }

  fun findById(id: Int) = pets.first { it.id == id }
}