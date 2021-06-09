import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ktor.graphql.Config
import ktor.graphql.graphQL

fun Application.main() {

  val server = embeddedServer(Netty, port = 8080) {
    routing {
      graphQL("/graphql", schema) { request ->
        Config(showExplorer = true)
      }
    }
  }

  server.start(wait = true)
}
