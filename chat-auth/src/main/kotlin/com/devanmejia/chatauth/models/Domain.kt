package com.devanmejia.chatauth.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Document("users")
data class User(
    @Id private val id: String? = null,
    val login: String,
    private val password: String,
    val email: String,
    var secretCode: String = "",
    var state: UserState = UserState.ACTIVE
) : UserDetails{

    fun setNewSecretCode(secretCode: String){
        this.secretCode = secretCode
        this.state = UserState.UNVERIFIED
    }
    fun discardSecretCode(){
        this.secretCode = ""
        this.state = UserState.ACTIVE
    }

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(state.name))

    override fun getPassword() = password

    override fun getUsername() = login

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}

enum class UserState {
    ACTIVE,
    UNVERIFIED
}
