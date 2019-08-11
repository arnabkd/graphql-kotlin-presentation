import java.lang.IllegalArgumentException
import java.util.*

object PeopleRepository {

    private var people = mutableListOf(
            Person("Tom", emptyList(), 1),
            Person("Jessica", emptyList(), 2)
    )

    fun findById(id: Int) =
            people.find {
                it.id == id
            } ?: throw IllegalArgumentException("Not found")

    fun all() =
            people.toList()

    fun addFriendConnection(id1: Int, id2: Int) {
        val p1 = findById(id1)
        val p2 = findById(id2)

        val up1 = p1.addFriend(p2)
        val up2 = p2.addFriend(p1)
        people.remove(p1)
        people.add(up1)
        people.remove(p2)
        people.add(up2)
    }

    fun changeName(id: Int, newName: String) = people.find {
        it.id == id
    }?.let {
        val updated = it.copy(name = newName)
        people.remove(it)
        people.add(updated)
        updated
    }?: throw IllegalArgumentException("Not found")

}