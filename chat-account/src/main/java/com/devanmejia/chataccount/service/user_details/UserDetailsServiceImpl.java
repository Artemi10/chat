package com.devanmejia.chataccount.service.user_details;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.service.crypto.CryptoService;
import com.devanmejia.chataccount.transfer.AuthenticationDTO;
import com.devanmejia.chataccount.transfer.TokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CryptoService cryptoService;
    private final RestTemplate restTemplate;
    private final String url;

    @Autowired
    public UserDetailsServiceImpl(RestTemplate restTemplate, CryptoService cryptoService,
                                  @Value("${api.auth}") String apiAuth) {
        this.restTemplate = restTemplate;
        this.cryptoService = cryptoService;
        this.url = String.format("%s/authentication", apiAuth);
    }

    @Override
    public UserDetails getUserDetails(String token) {
        String key = cryptoService.getPublicKey();
        TokenDTO tokenDTO = new TokenDTO(token, key);
        AuthenticationDTO encryptedData = getEncryptedData(tokenDTO);
        return cryptoService.decryptUserDetails(encryptedData);
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
