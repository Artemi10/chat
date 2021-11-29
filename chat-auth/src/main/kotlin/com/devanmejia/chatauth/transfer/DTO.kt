package com.devanmejia.chatauth.transfer

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

interface DTO{
    fun toJSON(): String = ObjectMapper().writeValueAsString(this)
}
data class CodeDTO(val login: String, val code: String) : DTO
data class EmailDTO(val address: String, val content: String) : DTO
data class LogInDTO(val login: String, val password: String) : DTO
data class SignUpDTO(val login: String, val password: String, val birthDate: Date, val email: String) : DTO
