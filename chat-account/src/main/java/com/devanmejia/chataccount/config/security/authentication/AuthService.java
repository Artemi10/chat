package com.devanmejia.chataccount.config.security.authentication;

import com.devanmejia.chataccount.config.security.auth_users.AuthUserState;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    boolean hasPermission(AuthUserState state);
    String getUserName();
}
