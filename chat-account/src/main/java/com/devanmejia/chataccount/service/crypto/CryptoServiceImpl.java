package com.devanmejia.chataccount.service.crypto;

import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.transfer.AuthenticationDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.security.*;

@Service
public class CryptoServiceImpl implements CryptoService{
    private final static String KEY_ALGORITHM = "RSA";
    private final static String DATA_ALGORITHM = "AES";

    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init() {
        KeyPair keyPair = generateRSAKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    private KeyPair generateRSAKeyPair() {
        SecureRandom secureRandom = new SecureRandom();
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(1024, secureRandom);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e){
            String msg = String.format("Can not find algorithm %s", KEY_ALGORITHM);
            throw new IllegalArgumentException(msg);
        }
    }

    @Override
    public String getPublicKey() {
        return DatatypeConverter.printBase64Binary(publicKey.getEncoded());
    }

    @Override
    public UserDetails decryptUserDetails(AuthenticationDTO authenticationDTO) {
        SecretKey secretKey = decryptKey(authenticationDTO.getKey());
        byte[] decryptedData = decryptData(authenticationDTO.getData(), secretKey);
        return deserializeUserDetails(decryptedData);
    }

    private SecretKey decryptKey(String encryptedKey) {
        try {
            Cipher decrypt = Cipher.getInstance(KEY_ALGORITHM);
            decrypt.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedData = decrypt.doFinal(DatatypeConverter.parseBase64Binary(encryptedKey));
            return getSecretKey(decryptedData);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException e){
            String msg = String.format("Can not decrypt key. %s", e.getMessage());
            throw new AuthException(msg);
        }
    }

    private SecretKey getSecretKey(byte[] key) {
        try {
            return new SecretKeySpec(key, DATA_ALGORITHM);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = String.format("Can not parse key. %s", e.getMessage());
            throw new AuthException(msg);
        }
    }

    private byte[] decryptData(String data, SecretKey secretKey) {
        try{
            Cipher decrypt = Cipher.getInstance(DATA_ALGORITHM);
            decrypt.init(Cipher.DECRYPT_MODE, secretKey);
            return decrypt.doFinal(DatatypeConverter.parseBase64Binary(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException
                | IllegalBlockSizeException | BadPaddingException e){
            String msg = String.format("Can not decrypt key. %s", e.getMessage());
            throw new AuthException(msg);
        }
    }

    private UserDetails deserializeUserDetails(byte[] byteArray){
        try {
            ObjectInput input = new ObjectInputStream(new ByteArrayInputStream(byteArray));
            return (UserDetails) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            String msg = String.format("Problem with deserialization. %s", e.getMessage());
            throw new AuthException(msg);
        }
    }
}
