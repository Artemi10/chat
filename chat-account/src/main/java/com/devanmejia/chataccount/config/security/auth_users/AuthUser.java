package com.devanmejia.chataccount.config.security.auth_users;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthUser implements UserDetails {
    @JsonProperty("password")
    private String password;
    @JsonProperty("state")
    private AuthUserState state;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("username")
    private String username;
    @JsonProperty("authorities")
    private List<Authority> authorities;
    @JsonProperty("accountNonLocked")
    private Boolean accountNonLocked;
    @JsonProperty("accountNonExpired")
    private Boolean accountNonExpired;
    @JsonProperty("credentialsNonExpired")
    private Boolean credentialsNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(state.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
