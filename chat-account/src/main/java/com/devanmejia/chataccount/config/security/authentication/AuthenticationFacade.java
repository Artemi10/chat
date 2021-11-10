package com.devanmejia.chataccount.config.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationFacade {
    Authentication getAuthentication();
}
