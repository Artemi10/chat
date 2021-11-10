package com.devanmejia.chataccount.config.security.user_details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface UserDetailsService {
    UserDetails getUserDetails(String token);
}
