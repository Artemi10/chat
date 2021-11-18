package com.devanmejia.chatauth.exceptions

class AuthException(message: String)
    : IllegalArgumentException("Auth Error: $message")

class EmailException(email: String)
    : IllegalArgumentException("Can not send message to $email")

class ConverterException(s: String)
    : IllegalArgumentException("Can not convert: $s")
