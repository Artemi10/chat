package com.devanmejia.chataccount.exception;

public class ConverterException extends IllegalArgumentException{
    public ConverterException(String s) {
        super("Can not convert: " + s);
    }
}
