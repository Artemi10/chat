package com.devanmejia.chatmessaging.service

import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.transfer.AuthenticationDTO
import com.devanmejia.chatmessaging.transfer.TokenDTO
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
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
                             @Value("\${api.auth}") private val apiAuth: String,
                             private val cryptoService: CryptoService
) : UserDetailsService {

    override suspend fun getUserDetails(token: String): UserDetails {
        val key = cryptoService.getPublicKey()
        val tokenDTO = TokenDTO(token, key)
        val encryptedData = getEncryptedData(tokenDTO)
        return cryptoService.decryptUserDetails(encryptedData)
    }

    private suspend fun getEncryptedData(tokenDTO: TokenDTO): AuthenticationDTO {
        try{
            return webClientBuilder.build().post()
                .uri("$apiAuth/authentication")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(tokenDTO.toJSON())
                .retrieve().awaitBody()
        } catch (ex: WebClientException){
            throw AuthException("Incorrect token")
        }
    }

    override fun isAuthenticated(token: String) = mono {
        getUserDetails(token)
            .authorities.map { it.authority }.contains("ACTIVE")
    }
}

