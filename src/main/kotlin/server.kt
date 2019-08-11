import io.ktor.application.Application
import io.ktor.routing.routing
import ktor.graphql.config
import ktor.graphql.graphQL

fun Application.main() {

  routing {
    //intercept(ApplicationCallPipeline.Call) {
    //    authenticate()
    //}

    graphQL("/graphql", schema) {
      config {
        graphiql = true
        formatError = formatErrorGraphQLError
      }
    }
  }
}
