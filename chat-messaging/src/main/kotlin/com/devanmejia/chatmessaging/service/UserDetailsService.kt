package com.devanmejia.chatmessaging.service

import com.devanmejia.chatmessaging.configuration.security.User
import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.transfer.AuthPayload
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientException
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono

@Service
interface UserDetailsService {
    fun isAuthenticated(token: String, chatId: Long): Mono<Boolean>
    suspend fun getUserDetails(token: String, chatId: Long): User
}

@Service
class UserDetailsServiceImpl(
    private val webClientBuilder: WebClient.Builder,
    @Value("\${api.auth}") private val apiAuth: String
) : UserDetailsService {

    override fun isAuthenticated(token: String, chatId: Long) = mono {
        val user = getUserDetails(token, chatId)
        user.authorities.map { it.authority }
            .contains("ACTIVE") && user.isEnabled
    }

    override suspend fun getUserDetails(token: String, chatId: Long): User {
        try{
            return webClientBuilder.build().post()
                .uri("$apiAuth/authentication")
                .header("Authorization", "Bearer_$token")
                .body(mono { chatId }, Long.javaClass)
                .retrieve().awaitBody()
        } catch (ex: WebClientException){
            throw AuthException("Incorrect token")
        }
    }
}

