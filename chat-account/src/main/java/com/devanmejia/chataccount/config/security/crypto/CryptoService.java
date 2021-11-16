package com.devanmejia.chataccount.config.security.crypto;

import com.devanmejia.chataccount.transfer.EncryptedObj;
import org.springframework.stereotype.Service;


@Service
public interface CryptoService<T> {
    EncryptedObj encryptObj(T obj, String keyStr);
}
