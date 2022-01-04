package com.devanmejia.chatauth.configuration.security

import com.devanmejia.chatauth.services.JWTSigner
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JWTAuthenticationManager(
    private val jwtSigner: JWTSigner) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication) = mono {
        val token = authentication.principal as String
        val login = jwtSigner.getLogin(token)
        jwtSigner.createAuthentication(login)
    }
}
