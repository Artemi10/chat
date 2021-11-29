package com.devanmejia.chataccount.config.security.user_details;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public UserInfoServiceImpl(RestTemplate restTemplate,
                               @Value("${api.auth}") String url) {
        this.restTemplate = restTemplate;
        this.apiUrl = String.format("%s/authentication", url);
    }

    @Override
    public UserInfo getUserInfo(String token) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", String.format("Bearer_%s", token));
            return restTemplate.exchange(apiUrl, HttpMethod.GET, new HttpEntity<>(headers), UserInfo.class).getBody();
        } catch (Exception e){
            throw new AuthException(e.getMessage());
        }
    }
}
