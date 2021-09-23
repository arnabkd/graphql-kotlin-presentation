import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import graphql.ExecutionInput
import graphql.GraphQL
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.authenticate
import io.ktor.auth.jwt.jwt
import io.ktor.auth.principal
import io.ktor.features.CORS
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondTextWriter
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.serialization.*
import kotlinx.serialization.*
import ktor.graphql.Config
import ktor.graphql.fromRequest
import ktor.graphql.graphQL
import java.util.Date

val jwtConfig = JwtConfig("secret")

fun Application.main() {
  val graphQLExecutor = GraphQL.newGraphQL(schema).build()

  val server = embeddedServer(Netty, port = 8080) {
    install(CallLogging)
    install(Authentication) {
      jwt {
        jwtConfig.configureKtorFeature(this)
      }
    }

    install(ContentNegotiation) {
      gson {
        setPrettyPrinting()
      }
    }


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
      // Set optional to true to allow introspection queries.
      // For all other queries, require auth.
      authenticate {
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

      post("/login") {
        val (username, password) = call.receive<LoginBody>()
        UserRepository.getUser(username, password)?.let { (username, userId) ->
          val token = jwtConfig.generateToken(JwtUser(userId, username))
          call.respond(token)
          return@post
        }
        call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
      }

      authenticate {
        get("/secret") {
          call.respond(mapOf("secret" to 42))
        }
      }
    }
  }


  server.start(wait = true)
}

data class LoginBody(val username: String, val password: String)
