package com.devanmejia.chatemail

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [MongoAutoConfiguration::class])
class ChatEmailApplication

fun main(args: Array<String>) {
    runApplication<ChatEmailApplication>(*args)
}
