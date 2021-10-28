package com.devanmejia.chateureka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class ChatEurekaApplication

fun main(args: Array<String>) {
    runApplication<ChatEurekaApplication>(*args)
}
