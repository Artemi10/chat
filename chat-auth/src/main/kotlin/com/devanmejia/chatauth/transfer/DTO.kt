package com.devanmejia.chatauth.transfer

import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.models.UserState
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

interface DTO{
    fun toJSON(): String = ObjectMapper().writeValueAsString(this)
}
data class CodeDTO(val login: String, val code: String) : DTO
data class TokenDTO(val jwt: String, val key: String) : DTO
data class EmailDTO(val address: String, val content: String) : DTO
data class LogInDTO(val login: String, val password: String) : DTO
data class SignUpDTO(val login: String, val password: String,
                     val birthDate: Date, val email: String) : DTO
data class AuthenticationDTO(val data: String, val key: String) : DTO

data class UserDTO(private val userName: String, private val password: String,
                   val state: UserState) : UserDetails, DTO {

    constructor(user: User) : this(user.login, user.password, user.state)

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(state.name))

    override fun getPassword() = password

    override fun getUsername() = userName

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
