package com.devanmejia.chatmessaging.controller

import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.model.Message
import com.devanmejia.chatmessaging.service.MessageService
import com.devanmejia.chatmessaging.service.UserDetailsService
import com.devanmejia.chatmessaging.transfer.AuthPayload
import io.rsocket.exceptions.RejectedSetupException
import kotlinx.coroutines.flow.Flow
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
@MessageMapping("api.messages")
class LiveStreamController(
    private val messageService: MessageService) {

//    @MessageMapping("stream")
//    suspend fun receive(@Payload messages: Flow<Message>) =
//        messageService.postMessageInLiveStream(messages)
//
//    @MessageMapping("stream")
//    suspend fun send() = messageService.getLiveMessageStream()
}

@Controller
class LiveStreamAuthController (
    private val userDetailsService: UserDetailsService) {

    @ConnectMapping
    fun onConnect(requester: RSocketRequester, @Payload payload: AuthPayload): Mono<Void> {
        try{
            return userDetailsService.isAuthenticated(payload.token, payload.chatId)
                .flatMap {
                    if (it) Mono.empty()
                    else Mono.error(RejectedSetupException("Connection is not authenticated"))
                }
        } catch (e: AuthException){
            throw RejectedSetupException("Connection is not authenticated")
        }
    }
}
