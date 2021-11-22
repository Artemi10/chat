package com.devanmejia.chatauth.services

import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.exceptions.EmailException
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.transfer.EmailDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitExchange

interface EmailService {
    suspend fun sendVerifyEmail(user: User, token: String)
    suspend fun sendVerifyEmail(email: String, code: String, token: String)
}

@Service
class EmailServiceImpl(private val webClientBuilder: WebClient.Builder,
                       @Value("\${api.email}")
                       private val apiEmail: String) : EmailService {

    override suspend fun sendVerifyEmail(user: User, token: String){
        val code = user.secretCode
        if (code != null){
            sendVerifyEmail(user.email, code, token)
        }
        else{
            throw AuthException("Secret code is not generated")
        }
    }

    override suspend fun sendVerifyEmail(email: String, code: String, token: String) {
        val emailDTO = EmailDTO(email, code)
        webClientBuilder.build().post()
            .uri("$apiEmail/verify")
            .header("Authorization", "Bearer_$token")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(emailDTO.toJSON())
            .awaitExchange {
                if (it.statusCode() != HttpStatus.OK)
                    throw EmailException(email)
            }

    }
}
