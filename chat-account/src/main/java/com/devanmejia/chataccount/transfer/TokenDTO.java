package com.devanmejia.chataccount.transfer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenDTO {
    private String jwt;
    private String key;
}
