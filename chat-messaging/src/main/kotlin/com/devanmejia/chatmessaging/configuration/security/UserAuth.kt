package com.devanmejia.chatmessaging.configuration.security

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    @JsonProperty("password")
    private val password: String,
    @JsonProperty("state")
    private val state: UserState,
    @JsonProperty("enabled")
    private val enabled: Boolean,
    @JsonProperty("username")
    private val username: String,
    @JsonProperty("authorities")
    private val authorities: List<Authority>,
    @JsonProperty("accountNonLocked")
    private val accountNonLocked: Boolean,
    @JsonProperty("accountNonExpired")
    private val accountNonExpired: Boolean,
    @JsonProperty("credentialsNonExpired")
    private val credentialsNonExpired: Boolean) : UserDetails{

    override fun getAuthorities() = mutableListOf(SimpleGrantedAuthority(state.name))

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = accountNonExpired

    override fun isAccountNonLocked() = accountNonLocked

    override fun isCredentialsNonExpired() = credentialsNonExpired

    override fun isEnabled() = enabled
}

data class Authority(@JsonProperty("authority") private val authority: String)

enum class UserState {
    ACTIVE,
    UNVERIFIED
}
