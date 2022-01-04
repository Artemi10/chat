package com.devanmejia.chatauth.repositories

import com.devanmejia.chatauth.configuration.soap.UserInfoClientRequest
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
interface UserRepository{
    suspend fun getChatUserInfo(login: String, chatId: Long): User?
    suspend fun getUserInfo(login: String): User?
    suspend fun getUserChatId(login: String): Set<Long>
    suspend fun saveUserInfo(user: User): User
}

@Service
class UserRepositoryImpl(private val webClient: WebClient,
                         private val converter: Converter<UserInfoDTO, User>,
                         @Value("\${api.account}") private val reportsApi: String,
                         @Value("\${api.account.password}") password: String,
                         @Value("\${api.account.username}") userName: String): UserRepository {

    private val headerContent = "<wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">\n" +
            "<wsse:UsernameToken>\n" +
            "   <wsse:Username>$userName</wsse:Username>\n" +
            "   <wsse:Password>$password</wsse:Password>\n" +
            "</wsse:UsernameToken>\n </wsse:Security>"

    override suspend fun getChatUserInfo(login: String, chatId: Long): User? {
        val requestBody = GetChatUserInfoRequest()
        requestBody.login = login
        requestBody.chatId = chatId
        val request = UserInfoClientRequest(headerContent, requestBody)
        return try{
            val responseBody = webClient.post()
                .uri(reportsApi)
                .contentType(MediaType.TEXT_XML)
                .body(mono { request }, request.javaClass)
                .retrieve().awaitBody<GetChatUserInfoResponse>().user
            converter.reconvert(responseBody)
        } catch (e: Exception){ null }
    }

    override suspend fun getUserInfo(login: String): User? {
        val requestBody = GetUserInfoRequest()
        requestBody.login = login
        val request = UserInfoClientRequest(headerContent, requestBody)
        return try{
            val responseBody = webClient.post()
                .uri(reportsApi)
                .contentType(MediaType.TEXT_XML)
                .body(mono { request }, request.javaClass)
                .retrieve().awaitBody<GetUserInfoResponse>().user
            converter.reconvert(responseBody)
        } catch (e: Exception){ null }
    }

    override suspend fun getUserChatId(login: String): Set<Long> {
        val requestBody = GetUserChatIdRequest()
        requestBody.login = login
        val request = UserInfoClientRequest(headerContent, requestBody)
        return try{
            webClient.post()
                .uri(reportsApi)
                .contentType(MediaType.TEXT_XML)
                .body(mono { request }, request.javaClass)
                .retrieve().awaitBody<GetUserChatIdResponse>()
                .chatIds.toSet()
        } catch (e: Exception){ emptySet() }
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
