package com.devanmejia.chataccount.exception;

public class EntityException extends IllegalArgumentException{
    public EntityException(String s) {
        super("Entity exception: " + s);
    }
}
