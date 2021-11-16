package com.devanmejia.chataccount.config.security.authentication;

import com.devanmejia.chataccount.model.user.State;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    boolean hasPermission(State state);
    String getUserName();
}
