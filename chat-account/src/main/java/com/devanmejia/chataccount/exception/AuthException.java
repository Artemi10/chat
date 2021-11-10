package com.devanmejia.chataccount.exception;

public class AuthException extends IllegalArgumentException{
    public AuthException(String s) {
        super(String.format("Auth message: %s", s));
    }
}
