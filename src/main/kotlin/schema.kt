import com.coxautodev.graphql.tools.GraphQLMutationResolver
import com.coxautodev.graphql.tools.GraphQLQueryResolver
import com.coxautodev.graphql.tools.SchemaParser
import com.google.common.io.Resources

val schema = createExecutableSchema()

@Suppress("UnstableApiUsage")
fun createExecutableSchema()= SchemaParser
  .newParser()
  .schemaString(
    Resources.toString(
      Resources.getResource("schema.graphql"),
      Charsets.UTF_8
    )
  )
  .dictionary( // Currently required for sealed classes
    mapOf(
    "Cat" to Cat::class.java,
    "Dog" to Dog::class.java
    )
  )
  .resolvers(QueryResolver(), MutationResolver())
  .build()
  .makeExecutableSchema()

@Suppress("unused") // GraphQL by reflection
class QueryResolver : GraphQLQueryResolver {
  fun person(id: Int) = PersonRepository.findById(id)
  fun allPeople() = PersonRepository.allPeople()
}

@Suppress("unused") // GraphQL by reflection
class MutationResolver : GraphQLMutationResolver