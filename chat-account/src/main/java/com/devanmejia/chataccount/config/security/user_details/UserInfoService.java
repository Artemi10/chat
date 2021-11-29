package com.devanmejia.chataccount.config.security.user_details;

import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {
    UserInfo getUserInfo(String token);
}
