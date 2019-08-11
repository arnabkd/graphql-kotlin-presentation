object PetsRepository {
  var pets = mutableListOf<Pet>()

  fun clear() = pets.clear()

  fun findById(id: Int) = pets.first { it.id == id }
  fun findByIds(ids: List<Int>) = pets
    .filter { it.id in ids}
    .also {
      println("Another expensive call to get pets with ids: $ids")
    }

  fun allPets() = pets.toList()

  fun addPet(pet: Pet) =
    if (pets.find { it.id == pet.id } != null)
      throw IllegalArgumentException("Already exists")
    else
      pets.add(pet)

  fun nextId() = (pets.maxByOrNull { it.id }?.id ?: 0) + 1
}
