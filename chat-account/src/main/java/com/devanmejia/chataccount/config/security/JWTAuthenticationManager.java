package com.devanmejia.chataccount.config.security;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.config.security.user_details.UserDetailsService;
import com.devanmejia.chataccount.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class JWTAuthenticationManager implements AuthenticationManager {
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    @Autowired
    public JWTAuthenticationManager(UserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        try{
            String login = userDetailsService.getUserLogin(token);
            UserDetails userDetails = userService.findByLogin(login);
            return new UsernamePasswordAuthenticationToken(userDetails,
                    "", userDetails.getAuthorities());
        } catch (AuthException e){
            return new UsernamePasswordAuthenticationToken(null, null);
        }
    }
}
