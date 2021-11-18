package com.devanmejia.chatauth.configuration.soap


import com.devanmejia.chatauth.exceptions.AuthException
import io.spring.guides.gs_producing_web_service.GetUserInfoRequest
import io.spring.guides.gs_producing_web_service.GetUserInfoResponse
import io.spring.guides.gs_producing_web_service.RequestBody
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Component
class UserInfoClient(
    private val webClient: WebClient,
    @Value("\${api.account}") private val reportsApi: String,
    @Value("\${api.account.username}") userName: String,
    @Value("\${api.account.password}") password: String) {

    private val headerContent = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
            "<wsse:UsernameToken>\n" +
            "   <wsse:Username>$userName</wsse:Username>\n" +
            "   <wsse:Password>$password</wsse:Password>\n" +
            "</wsse:UsernameToken>\n </wsse:Security>"

    suspend fun getUserInfo(login: String, publicKey: String): Pair<String, String> {
        val requestBody = GetUserInfoRequest()
        requestBody.login = login
        requestBody.publicKey = publicKey
        val request = UserInfoClientRequest(headerContent, requestBody)
        try{
            val response = webClient.post()
                .uri(reportsApi)
                .contentType(MediaType.TEXT_XML)
                .body(mono { request }, request.javaClass)
                .retrieve().awaitBody<GetUserInfoResponse>()
            return Pair(response.encryptedUser, response.encryptedKey)
        } catch (e: Exception){
            throw AuthException("Invalid user: ${e.message}")
        }
    }
}
