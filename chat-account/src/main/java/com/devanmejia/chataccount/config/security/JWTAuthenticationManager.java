package com.devanmejia.chataccount.config.security;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.service.user_details.UserDetailsService;
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

    @Autowired
    public JWTAuthenticationManager(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        try{
            UserDetails userDetails = userDetailsService.getUserDetails(token);
            return new UsernamePasswordAuthenticationToken(userDetails,
                    "", userDetails.getAuthorities());
        } catch (AuthException e){
            return new UsernamePasswordAuthenticationToken(null, null);
        }
    }
}
