package com.devanmejia.chataccount.config.security;

import com.devanmejia.chataccount.config.security.user_details.UserInfoService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.service.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

@Configuration
public class TokenAuthenticationManager implements AuthenticationManager {
    private final UserInfoService userInfoService;
    private final UserService userService;
    private final String serviceName;

    public TokenAuthenticationManager(UserInfoService userInfoService, UserService userService,
                                      @Value("${api.auth-service.name}") String serviceName) {
        this.userInfoService = userInfoService;
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
            UserDetails userDetails = userInfoService.getUserInfo(token);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (AuthException e){
            return new UsernamePasswordAuthenticationToken(null, null);
        }
    }
}
