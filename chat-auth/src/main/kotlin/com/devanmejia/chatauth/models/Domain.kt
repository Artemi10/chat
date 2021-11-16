package com.devanmejia.chatauth.models

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    @JsonProperty("login")
    val login: String,
    @JsonProperty("password")
    private val password: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("secretCode")
    var secretCode: String = "",
    @JsonProperty("state")
    var state: UserState = UserState.ACTIVE,
    @JsonProperty("enabled")
    private val enabled: Boolean = true,
    @JsonProperty("accountNonLocked")
    private val accountNonLocked: Boolean = true,
    @JsonProperty("accountNonExpired")
    private val accountNonExpired: Boolean = true,
    @JsonProperty("credentialsNonExpired")
    private val credentialsNonExpired: Boolean = true
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

    override fun isAccountNonExpired() = accountNonExpired

    override fun isAccountNonLocked() = accountNonLocked

    override fun isCredentialsNonExpired() = credentialsNonExpired

    override fun isEnabled() = enabled
}

enum class UserState {
    ACTIVE,
    UNVERIFIED
}
