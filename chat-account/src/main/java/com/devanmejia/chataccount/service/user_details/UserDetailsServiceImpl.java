package com.devanmejia.chataccount.service.user_details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final String apiAuth;

    @Autowired
    public UserDetailsServiceImpl(RestTemplate restTemplate,
                                  @Value("${api.auth}") String apiAuth) {
        this.restTemplate = restTemplate;
        this.apiAuth = apiAuth;
    }

    @Override
    public UserDetails getUserDetails(String token) {
        return null;
    }
}
