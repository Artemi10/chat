package com.devanmejia.chateureka.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig {
    fun corsConfigurer(): WebMvcConfigurer = object : WebMvcConfigurer{
        override fun addCorsMappings(registry: CorsRegistry) {
            registry.addMapping("/**")
                .allowedMethods("GET, POST, PUT, DELETE, PATCH")
                .allowedHeaders("*")
                .allowedOrigins("*")
        }
    }
}
