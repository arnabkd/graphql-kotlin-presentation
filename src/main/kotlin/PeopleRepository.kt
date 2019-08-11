import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.BatchLoader
import org.dataloader.DataLoader
import java.lang.IllegalArgumentException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

    fun all(keys: List<Int>) = keys.map { findById(it) }

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

}

object PersonBatchLoader : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

    operator fun invoke(personService: PeopleRepository) =
            BatchLoader<Int, Person> { keys ->
                future(coroutineContext) {
                    personService.all(keys)
                            .let { result ->
                                keys.map { result[it] }
                            }
                }
            }

    const val key = "Person"
}