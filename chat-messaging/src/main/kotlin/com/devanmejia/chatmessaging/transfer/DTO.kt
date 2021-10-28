package com.devanmejia.chatmessaging.transfer

import com.fasterxml.jackson.databind.ObjectMapper

interface DTO{
    fun toJSON(): String = ObjectMapper().writeValueAsString(this)
}
data class TokenDTO(val jwt: String, val key: String) : DTO
data class AuthenticationDTO(val data: String, val key: String) : DTO
