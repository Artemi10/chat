package com.devanmejia.chatauth.configuration.soap

import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.services.Converter
import io.spring.guides.gs_producing_web_service.*
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
interface UserInfoClient{
    suspend fun getUserInfo(login: String): User?
    suspend fun saveUserInfo(user: User): User
}

@Service
class UserInfoClientImpl(private val webClient: WebClient,
                         private val converter: Converter<UserInfoDTO, User>,
                         @Value("\${api.account}") private val reportsApi: String,
                         @Value("\${api.account.password}") password: String,
                         @Value("\${api.account.username}") userName: String): UserInfoClient {

    private val headerContent = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
            "<wsse:UsernameToken>\n" +
            "   <wsse:Username>$userName</wsse:Username>\n" +
            "   <wsse:Password>$password</wsse:Password>\n" +
            "</wsse:UsernameToken>\n </wsse:Security>"

    override suspend fun getUserInfo(login: String): User? {
        val requestBody = GetUserInfoRequest()
        requestBody.login = login
        val request = UserInfoClientRequest(headerContent, requestBody)
        try{
            val responseBody = webClient.post()
                .uri(reportsApi)
                .contentType(MediaType.TEXT_XML)
                .body(mono { request }, request.javaClass)
                .retrieve().awaitBody<GetUserInfoResponse>().user
            return converter.reconvert(responseBody)
        } catch (e: Exception){
            return null
        }
    }

    override suspend fun saveUserInfo(user: User): User {
        val requestBody = SaveUserInfoRequest()
        requestBody.userInfo = converter.convert(user)
        val request = UserInfoClientRequest(headerContent, requestBody)
        return try{
            val responseBody = webClient.post()
                .uri(reportsApi)
                .contentType(MediaType.TEXT_XML)
                .body(mono { request }, request.javaClass)
                .retrieve().awaitBody<SaveUserInfoResponse>().userInfo
            converter.reconvert(responseBody)
        } catch (e: Exception){
            throw AuthException("Can not save user. ${e.message}")
        }
    }
}
