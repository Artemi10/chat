package com.devanmejia.chatauth.configuration.security.soap

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller

@Configuration
class UserInfoClientConfig(
    @Value("\${api.account}")
    private val reportsApi: String
) {
    @Bean
    fun marshaller(): Jaxb2Marshaller {
        val marshaller = Jaxb2Marshaller()
        marshaller.contextPath = "io.spring.guides.gs_producing_web_service"
        return marshaller
    }

    @Bean
    fun userInfoClient(marshaller: Jaxb2Marshaller): UserInfoClient {
        val client = UserInfoClient(reportsApi)
        client.defaultUri = reportsApi
        client.marshaller = marshaller
        client.unmarshaller = marshaller
        return client
    }
}
