import graphql.ExecutionInput
import graphql.GraphQL
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ktor.graphql.Config
import ktor.graphql.fromRequest
import ktor.graphql.graphQL

fun Application.main() {
  val graphQLExecutor = GraphQL.newGraphQL(schema).build()


  val server = embeddedServer(Netty, port = 8080) {
    // CORS for localhost
    install(CORS) {
      method(HttpMethod.Options)
      method(HttpMethod.Put)
      method(HttpMethod.Delete)
      method(HttpMethod.Patch)
      header(HttpHeaders.Authorization)
      header(HttpHeaders.ContentType)
      // header("any header") if you want to add any header
      allowCredentials = true
      allowNonSimpleContentTypes = true
      host("localhost:3000", listOf("http", "https"))
      println("CORS enabled for $hosts")
    }

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
