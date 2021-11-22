package com.devanmejia.chataccount.config.security;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.config.security.user_details.UserDetailsService;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class TokenAuthenticationManager implements AuthenticationManager {
    private final UserDetailsService userDetailsService;
    private final UserService userService;
    private final String serviceName;

    @Autowired
    public TokenAuthenticationManager(UserDetailsService userDetailsService, UserService userService,
                                      @Value("${api.auth-service.name}") String serviceName) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.serviceName = serviceName;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal().equals(serviceName)){
            UserDetails userDetails = userService.findByLogin(serviceName);
            if (userDetails.getPassword().equals(authentication.getCredentials())){
                return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            }
            else {
                return new UsernamePasswordAuthenticationToken(null, null);
            }
        }
        try{
            String token = (String) authentication.getCredentials();
            String login = userDetailsService.getUserLogin(token);
            UserDetails userDetails = userService.findByLogin(login);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (AuthException e){
            return new UsernamePasswordAuthenticationToken(null, null);
        }
    }
}
