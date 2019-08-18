import org.amshove.kluent.`should contain all`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it

object AddPersonSpek : Spek({
  afterGroup {
    PeopleRepository.clearPersonList()
  }

  describe(""){
    val mutationResolver = MutationResolver()
    val queryResolver = QueryResolver()

    given("adding a person"){
      val expectedToActual = (1..100).map {
        PersonInput("Person:$it")
      }.map {
        it.person to mutationResolver.addPerson(it)
      }

      it("the person should be added and returned"){
        expectedToActual.forEach {(expected, actual) ->
          expected `should equal` actual
        }
      }

      it("the person should be queryable") {
        queryResolver.allPeople() `should contain all` expectedToActual.map { it.first }
        expectedToActual.forEach {
          queryResolver.findById(it.first.id) `should not be` null
        }
      }
    }
  }
})

