package com.devanmejia.chataccount.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EncryptedObj {
    private String encryptedData;
    private String encryptedKey;
}
