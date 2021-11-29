package com.devanmejia.chatmessaging.configuration.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JWTServerAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        if (exchange != null){
            val authHeader = exchange.request.headers["Authorization"]
            val chatHeader = exchange.request.headers["Chat"]
            if (authHeader != null && authHeader[0].startsWith("Bearer_") && chatHeader != null){
                return try{
                    val token = authHeader[0].substring(7)
                    val chatId = chatHeader[0].toLong()
                    Mono.just(UsernamePasswordAuthenticationToken(token, chatId))
                } catch (e: Exception){ Mono.empty() }
            }
        }
        return Mono.empty()
    }
}
