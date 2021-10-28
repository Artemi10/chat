package com.devanmejia.chatmessaging.configuration.security

import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.service.UserDetailsService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JWTAuthenticationManager(private val userDetailsService: UserDetailsService)
    : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return mono {
            val token = authentication.credentials as String
            try {
                val userDetails = userDetailsService.getUserDetails(token)
                UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
            } catch (ex: AuthException){
                UsernamePasswordAuthenticationToken(null, null)
            }
        }
    }
}

