package com.devanmejia.chatgateway.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.cors.reactive.CorsUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
class CorsConfig {
    companion object{
        const val ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Content-Length, Authorization, credential, X-XSRF-TOKEN"
        const val ALLOWED_METHODS = "GET, PUT, POST, DELETE, OPTIONS, PATCH"
        const val ALLOWED_ORIGIN = "*"
    }

    @Bean
    fun corsFilter(): WebFilter{
        return object : WebFilter{
            override fun filter(ctx: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
                val request = ctx.request
                if (CorsUtils.isCorsRequest(request)){
                    val response = ctx.response
                    val headers = response.headers
                    headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN)
                    headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS)
                    headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS)
                    if (request.method == HttpMethod.OPTIONS) {
                        response.statusCode = HttpStatus.OK
                        return Mono.empty()
                    }
                }
                return chain.filter(ctx)
            }
        }
    }
}
