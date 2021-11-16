package com.devanmejia.chatauth.configuration.security

import com.devanmejia.chatauth.models.UserState
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter


@Configuration
class SecurityConfiguration {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity, jwtAuthenticationManager: ReactiveAuthenticationManager,
                               jwtAuthenticationConverter: ServerAuthenticationConverter): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)
        return http
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }
    @Bean
    fun serverHttpSecurity(): ServerHttpSecurity{
        return ServerHttpSecurity.http()
            .authorizeExchange()
            .pathMatchers("/api/auth/verify", "/api/auth/code").hasAuthority(UserState.UNVERIFIED.name)
            .anyExchange().permitAll().and()
            .httpBasic().disable()
            .csrf().disable()
            .cors().disable()
            .formLogin().disable()
            .logout().disable()
    }
}
