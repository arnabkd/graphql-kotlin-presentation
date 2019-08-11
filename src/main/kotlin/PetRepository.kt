object PetRepository {

  private var pets: MutableList<Pet> = mutableListOf(
    Pet.Dog(1, "Rocket"),
    Pet.Cat(2, "Sherlock")
  )

  fun findById(id: Int) =
    pets.find {
      it.id == id
    } ?: throw IllegalArgumentException("Not found")

  fun all() =
    pets.toList()

  fun all(petIds: List<Int>) =
    all().filter {
      it.id in petIds
    }

}