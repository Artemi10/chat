package com.devanmejia.chatauth.configuration.security

import com.devanmejia.chatauth.services.jwt.JWTSigner
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JWTServerAuthenticationConverter(val jwtSigner: JWTSigner) : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        if (exchange != null){
            val token = jwtSigner.getJWT(exchange)
            return if (token.isEmpty()) Mono.empty()
            else Mono.just(UsernamePasswordAuthenticationToken(token, token))
        }
        return Mono.empty()
    }

}
