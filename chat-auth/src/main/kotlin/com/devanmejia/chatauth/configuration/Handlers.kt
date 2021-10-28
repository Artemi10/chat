package com.devanmejia.chatauth.configuration

import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.exceptions.EmailException
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

@ControllerAdvice
class EmailExceptionHandler {

    @ExceptionHandler(EmailException::class)
    fun handleAuthException(exchange: ServerWebExchange, ex: EmailException): Mono<Void> {
        val response: ServerHttpResponse = exchange.response
        val dataBuffer: DataBuffer = response.bufferFactory()
            .wrap(ex.message!!.toByteArray(StandardCharsets.UTF_8))
        response.statusCode = HttpStatus.NOT_FOUND
        return response.writeWith(Mono.just(dataBuffer))
    }
}
