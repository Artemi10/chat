package com.devanmejia.chatmessaging.transfer

import com.devanmejia.chatmessaging.model.Message


data class AuthPayload(val token: String)
data class DataPayLoad(val message: Message, val token: String)
