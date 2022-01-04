package com.devanmejia.chatmessaging.service

import com.devanmejia.chatmessaging.configuration.security.User
import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.transfer.AuthPayload
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
    suspend fun isAuthenticated(token: String): Boolean
    suspend fun getUserDetails(token: String): User
    suspend fun isAuthenticated(token: String, chatId: Long): Boolean
    suspend fun getUserDetails(token: String, chatId: Long): User
    suspend fun getAvailableChatIds(token: String): Set<Long>
}

@Service
class UserDetailsServiceImpl(
    private val webClientBuilder: WebClient.Builder,
    @Value("\${api.auth}") private val apiAuth: String
) : UserDetailsService {

    override suspend fun isAuthenticated(token: String, chatId: Long): Boolean{
        val user = getUserDetails(token, chatId)
        return isAuthenticated(user)
    }

    override suspend fun isAuthenticated(token: String) =
        isAuthenticated(getUserDetails(token))

    private fun isAuthenticated(user: UserDetails) =
        user.authorities.map { it.authority }.contains("ACTIVE") && user.isEnabled

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

    override suspend fun getUserDetails(token: String): User {
        try{
            return webClientBuilder.build().get()
                .uri("$apiAuth/authentication")
                .header("Authorization", "Bearer_$token")
                .retrieve().awaitBody()
        } catch (ex: WebClientException){
            throw AuthException("Incorrect token")
        }
    }

    override suspend fun getAvailableChatIds(token: String): Set<Long> {
        try{
            return webClientBuilder.build().get()
                .uri("$apiAuth/authentication/chats")
                .header("Authorization", "Bearer_$token")
                .retrieve().awaitBody()
        } catch (ex: WebClientException){
            throw AuthException("Incorrect token")
        }
    }
}

