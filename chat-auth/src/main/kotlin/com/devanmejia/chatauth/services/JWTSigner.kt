package com.devanmejia.chatauth.services.jwt

import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.services.user.UserService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.server.ServerWebExchange
import java.security.*
import java.time.Duration
import java.time.Instant
import java.util.*


interface JWTSigner {
    fun createJWT(user: User): String
    fun getLogin(token: String): String
    fun getLogin(exchange: ServerWebExchange): String
    fun getJWT(exchange: ServerWebExchange): String
    suspend fun createAuthentication(login: String): Authentication
}

@Service
class JWTSignerImpl(private val userService: UserService) : JWTSigner {

    companion object{
        private const val ALGORITHM = "RSA"
    }

    private val privateKey: PrivateKey
    private val publicKey: PublicKey

    init {
        val keyPair = generateRSAKeyPair()
        privateKey = keyPair.private
        publicKey = keyPair.public
    }

    private fun generateRSAKeyPair(): KeyPair {
        val secureRandom = SecureRandom()
        val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
        keyPairGenerator.initialize(2048, secureRandom)
        return keyPairGenerator.generateKeyPair()
    }

    override fun createJWT(user: User): String {
        val claims = Jwts.claims().setSubject(user.login)
        claims["state"] = user.state.name
        return Jwts.builder()
            .signWith(privateKey)
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(getExpirationDate())
            .compact()
    }

    override fun getLogin(token: String): String = validateJWT(token).body.subject
    override fun getLogin(exchange: ServerWebExchange): String = getLogin(getJWT(exchange))

    override fun getJWT(exchange: ServerWebExchange): String{
        val authHeader = exchange.request.headers["Authorization"]
        return if (authHeader != null && authHeader[0].startsWith("Bearer_")){
            authHeader[0].substring(7)
        } else ""
    }

    override suspend fun createAuthentication(login: String): Authentication {
        val userDetails = userService.getUserByLogin(login)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun validateJWT(jwt: String): Jws<Claims> {
        try{
            return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt)
        } catch (e: Exception){
            throw AuthException("Can not parse token")
        }
    }

    private fun getExpirationDate() =
        Date.from(Instant.now().plus(Duration.ofMinutes(15)))
}
