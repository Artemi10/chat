package com.devanmejia.chataccount.config.security.user_details;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.transfer.AuthenticationDTO;
import com.devanmejia.chataccount.transfer.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public UserDetailsServiceImpl(RestTemplate restTemplate,
                                  @Value("${api.auth}") String apiAuth) {
        this.restTemplate = restTemplate;
        this.url = String.format("%s/authentication", apiAuth);
    }

    @Override
    public String getUserLogin(String token) {
        return null;
    }

    private AuthenticationDTO getEncryptedData(TokenDTO tokenDTO) {
        try{
            return restTemplate.postForObject(url, tokenDTO, AuthenticationDTO.class);
        } catch (Exception e){
            String msg = String.format("Incorrect token: %s", e.getMessage());
            throw new AuthException(msg);
        }
    }
}
