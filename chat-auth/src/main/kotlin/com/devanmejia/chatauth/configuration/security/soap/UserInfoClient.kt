package com.devanmejia.chatauth.configuration.security.soap

import io.spring.guides.gs_producing_web_service.GetUserInfoRequest
import io.spring.guides.gs_producing_web_service.GetUserInfoResponse
import org.springframework.ws.client.core.support.WebServiceGatewaySupport
import org.springframework.ws.soap.client.core.SoapActionCallback

class UserInfoClient(
    private val reportsApi: String) : WebServiceGatewaySupport() {

    fun getUserInfo(login: String, publicKey: String): Pair<String, String> {
        val request = GetUserInfoRequest()
        request.login = login
        request.publicKey = publicKey
        val response = webServiceTemplate
            .marshalSendAndReceive(request, SoapActionCallback(reportsApi)) as GetUserInfoResponse
        return Pair(response.encryptedUser, response.encryptedKey)
    }
}
