package com.devanmejia.chatemail.configuration

import com.devanmejia.chatemail.exceptions.AuthException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@ControllerAdvice
class AuthExceptionHandler {

    @ExceptionHandler(AuthException::class)
    fun handleAuthException(exchange: ServerWebExchange, ex: AuthException): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        val dataBuffer: DataBuffer = response.bufferFactory()
            .wrap(ex.message!!.toByteArray(StandardCharsets.UTF_8))
        response.statusCode = HttpStatus.FORBIDDEN
        return response.writeWith(Mono.just(dataBuffer))
    }
}
