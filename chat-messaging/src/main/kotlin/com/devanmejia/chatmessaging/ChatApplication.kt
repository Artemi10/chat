package com.devanmejia.chatmessaging

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
class ChatApplication

fun main(args: Array<String>) {
    Hooks.onErrorDropped {}
    runApplication<ChatApplication>(*args)
}
