package com.devanmejia.chataccount.config.security.crypto;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.transfer.EncryptedObj;
import com.devanmejia.chataccount.transfer.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

@Service
public class AuthUserCryptoService implements CryptoService<UserInfo> {
    private static final String KEY_ALGORITHM = "RSA";
    private static final String DATA_ALGORITHM = "AES";

    @Override
    public EncryptedObj encryptObj(UserInfo obj, String keyStr) {
        try {
            SecretKey dataKey = generateDataEncryptionKey();
            String encryptedData = encryptData(new ObjectMapper().writeValueAsBytes(obj), dataKey);
            String encryptedKey = encryptKey(dataKey.getEncoded(), convert(keyStr));
            return new EncryptedObj(encryptedData, encryptedKey);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new AuthException(String.format("Can convert to JSON. %s", e.getMessage()));
        }
    }

    private SecretKey generateDataEncryptionKey() {
        try{
            KeyGenerator keyGenerator = KeyGenerator.getInstance(DATA_ALGORITHM);
            keyGenerator.init(256);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e){
            throw new AuthException(String.format("Can not find algorithm. %s", e.getMessage()));
        }
    }

    private PublicKey convert(String keyStr){
        byte[] encoded = DatatypeConverter.parseBase64Binary(keyStr);
        KeySpec keySpec = new X509EncodedKeySpec(encoded);
        try {
            return KeyFactory.getInstance(KEY_ALGORITHM)
                    .generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthException(String.format("Can not parse key. %s", e.getMessage()));
        }
    }

    public String encryptData(byte[] data, SecretKey key) {
        try{
           Cipher cipher = Cipher.getInstance(DATA_ALGORITHM);
           cipher.init(Cipher.ENCRYPT_MODE, key);
            return DatatypeConverter.printBase64Binary(cipher.doFinal(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException e){
            e.printStackTrace();
            throw new AuthException(String.format("Can not encrypt data. %s", e.getMessage()));
        }
    }

    private String encryptKey(byte[] key,  PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return DatatypeConverter.printBase64Binary(cipher.doFinal(key));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e){
            throw new AuthException(String.format("Can not find algorithm. %s", e.getMessage()));
        }
    }

}
