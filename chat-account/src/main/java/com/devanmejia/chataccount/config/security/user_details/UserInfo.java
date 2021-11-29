package com.devanmejia.chataccount.config.security.user_details;

import com.devanmejia.chataccount.model.user.State;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Setter
@NoArgsConstructor
public class UserInfo implements UserDetails {
    @JsonProperty("password")
    private String password;
    @JsonProperty("state")
    private State state;
    @JsonProperty("enabled")
    private boolean enabled;
    @JsonProperty("username")
    private String username;
    @JsonProperty("authorities")
    private List<Authority> authorities;
    @JsonProperty("accountNonLocked")
    private boolean accountNonLocked;
    @JsonProperty("accountNonExpired")
    private boolean accountNonExpired;
    @JsonProperty("credentialsNonExpired")
    private boolean credentialsNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(state.name()));
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
