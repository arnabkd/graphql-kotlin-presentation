import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.Principal
import io.ktor.auth.jwt.JWTAuthenticationProvider
import java.util.Date

class JwtConfig(jwtSecret: String) {

  companion object Constants {

    private const val jwtIssuer = "com.example"
    private const val jwtRealm = "com.example.people"
    private const val expireTime = 15 * 60 * 1000L // expire after 15 minutes

    // claims
    private const val CLAIM_USERID = "userId"
    private const val CLAIM_USERNAME = "username"
  }

  private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
  private val jwtVerifier = JWT.require(jwtAlgorithm).withIssuer(jwtIssuer).build()

  fun generateToken(user: JwtUser): String = JWT.create()
    .withSubject("Authentication")
    .withIssuer(jwtIssuer)
    .withExpiresAt(Date(System.currentTimeMillis() + expireTime))
    .withClaim(CLAIM_USERID, user.userId)
    .withClaim(CLAIM_USERNAME, user.username)
    .sign(jwtAlgorithm)

  fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
    verifier(jwtVerifier)
    realm = jwtRealm

    validate {
      val userId = it.payload.getClaim(CLAIM_USERID).asInt()
      val username = it.payload.getClaim(CLAIM_USERNAME).asString()

      if (userId != null && username != null) JwtUser(userId, username)
      else null
    }
  }
}

data class JwtUser(val userId: Int, val username: String) : Principal
