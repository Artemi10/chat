package com.devanmejia.chatmessaging.controller

import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.model.Message
import com.devanmejia.chatmessaging.service.MessageService
import com.devanmejia.chatmessaging.service.UserDetailsService
import com.devanmejia.chatmessaging.transfer.AuthPayload
import com.devanmejia.chatmessaging.transfer.DataPayLoad
import io.rsocket.exceptions.RejectedSetupException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.mono
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.annotation.ConnectMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
@MessageMapping("api.messages")
class LiveStreamController(
    private val messageService: MessageService,
    private val userDetailsService: UserDetailsService) {

    @MessageMapping("stream")
    suspend fun receive(@Payload dataFlow: Flow<DataPayLoad>) {
        val messageFlow = dataFlow
            .filter { userDetailsService.isAuthenticated(it.token, it.message.chatId) }
            .map { it.message }
        messageService.postMessageInLiveStream(messageFlow)
    }

    @MessageMapping("stream")
    suspend fun send(@Payload payload: AuthPayload): Flow<Message> {
        if (userDetailsService.isAuthenticated(payload.token)){
            val chatIds = userDetailsService.getAvailableChatIds(payload.token)
            return messageService.getLiveMessageStream(chatIds)
        }
        throw AuthException("Not permit")
    }
}

@Controller
class LiveStreamAuthController (
    private val userDetailsService: UserDetailsService) {

    @ConnectMapping
    fun onConnect(requester: RSocketRequester, @Payload payload: AuthPayload): Mono<Void> {
        try{
            return mono { userDetailsService.isAuthenticated(payload.token) }
                .flatMap {
                    if (it) Mono.empty()
                    else Mono.error(RejectedSetupException("Connection is not authenticated"))
                }
        } catch (e: AuthException){
            throw RejectedSetupException("Connection is not authenticated")
        }
    }
}
