package com.devanmejia.chataccount.config.security.user_details;

import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService {
    String getUserLogin(String token);
}
