package com.devanmejia.chatmessaging.service

import com.devanmejia.chatmessaging.configuration.security.User
import com.devanmejia.chatmessaging.exception.AuthException
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

@Service
interface UserDetailsService {
    suspend fun getUserDetails(token: String): UserDetails
    fun isAuthenticated(token: String): Mono<Boolean>
}

@Service
class UserDetailsServiceImpl(private val webClientBuilder: WebClient.Builder,
                             @Value("\${api.auth}") private val apiAuth: String
) : UserDetailsService {

    override suspend fun getUserDetails(token: String): User {
        try{
            return webClientBuilder.build().get()
                .uri("$apiAuth/authentication")
                .header("Authorization", "Bearer_${token}")
                .retrieve().awaitBody()
        } catch (ex: WebClientException){
            throw AuthException("Incorrect token")
        }
    }

    override fun isAuthenticated(token: String) = mono {
        getUserDetails(token)
            .authorities.map { it.authority }
            .contains("ACTIVE")
    }
}

