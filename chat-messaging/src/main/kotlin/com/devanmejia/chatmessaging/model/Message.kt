package com.devanmejia.chatmessaging.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("messages")
data class Message(
    @Id private val id: String? = null,
    val login: String,
    val content: String
    )
