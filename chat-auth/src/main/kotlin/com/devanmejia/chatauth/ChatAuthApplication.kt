package com.devanmejia.chatauth


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
class ChatAuthApplication

fun main(args: Array<String>) {
    runApplication<ChatAuthApplication>(*args)
}
