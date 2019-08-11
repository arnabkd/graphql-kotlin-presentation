import graphql.ExecutionInput
import graphql.GraphQL
import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ktor.graphql.Config
import ktor.graphql.fromRequest
import ktor.graphql.graphQL

fun Application.main() {
  val graphQLExecutor = GraphQL.newGraphQL(schema).build()

  val server = embeddedServer(Netty, port = 8080) {
    routing {
      graphQL("/graphql", schema) { request ->
        Config(
          formatError = formatErrorGraphQLError,
          showExplorer = true,
          executeRequest = {
            // Building registry per request ensures that we don't share data across requests
            val dataloaderRegistry = buildRegistry()
            val input = ExecutionInput.newExecutionInput()
              .fromRequest(request)
              .dataLoaderRegistry(dataloaderRegistry)
              .build()

            graphQLExecutor.execute(input)
          }
        )
      }
    }
  }

  server.start(wait = true)
}
