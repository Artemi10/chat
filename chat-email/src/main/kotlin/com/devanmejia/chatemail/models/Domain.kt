package com.devanmejia.chatemail.models

data class Email(val address: String, val content: String)

enum class MessageTemplate {
    VERIFY_MESSAGE
}
