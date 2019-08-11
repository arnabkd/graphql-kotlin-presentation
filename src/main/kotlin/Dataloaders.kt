import graphql.schema.DataFetchingEnvironment
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderRegistry
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.slf4j.MDCContext
import org.dataloader.BatchLoader

fun buildRegistry() = DataLoaderRegistry().apply {
  register(PetBatchLoader.key, DataLoader(PetBatchLoader()))
  register(PersonBatchLoader.key, DataLoader(PersonBatchLoader()))
}

object PersonBatchLoader : CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = MDCContext()

  operator fun invoke() =
    BatchLoader<Int, Person> { keys ->
      future(coroutineContext) {
        val people = PersonRepository.findByIds(keys).associateBy { it.id }
        keys
          .map {
            people.getValue(it)
          }
      }
    }

  const val key = "Person"
}

object PetBatchLoader : CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = MDCContext()

  operator fun invoke() =
    BatchLoader<Int, Pet> { keys ->
      future(coroutineContext) {
        val pets = PetsRepository.findByIds(keys).associateBy { it.id }
        keys
          .map {
            pets.getValue(it)
          }
      }
    }

  const val key = "Pet"
}

// Define loader as extension method to ensure types are used consistently.
fun DataFetchingEnvironment.getPersonDataLoader(): DataLoader<Int, Person> =
  dataLoaderRegistry
    .getDataLoader(PersonBatchLoader.key)

fun DataFetchingEnvironment.getPetDataLoader(): DataLoader<Int, Pet> =
  dataLoaderRegistry
    .getDataLoader(PetBatchLoader.key)

