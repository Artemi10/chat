package com.devanmejia.chataccount.exception;

public class NotFoundException extends IllegalArgumentException{
    public NotFoundException(String s) {
        super("Not found exception: " + s);
    }
}
