package com.devanmejia.chataccount.config.security.crypto;

import com.devanmejia.chataccount.transfer.AuthenticationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface CryptoService {
    String getPublicKey();
    UserDetails decryptUserDetails(AuthenticationDTO authenticationDTO);
}
