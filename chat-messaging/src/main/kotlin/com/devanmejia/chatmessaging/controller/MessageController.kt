package com.devanmejia.chatmessaging.controller

import com.devanmejia.chatmessaging.service.MessageService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/message")
class MessageController(private val messageService: MessageService) {
    private val messageAmountPerPage = 10

    @GetMapping
    suspend fun getLatestMessages(authentication: Authentication) =
        messageService.getNextLatestMessages(messageAmountPerPage, authentication.credentials as Long)

    @GetMapping("/skip/{skipAmount}")
    suspend fun getNextLatestMessages(@PathVariable skipAmount: Int, authentication: Authentication) =
        messageService.getNextLatestMessages(messageAmountPerPage, authentication.credentials as Long)

    @GetMapping("/find/{phrase}")
    suspend fun getMessagesByPhrase(@PathVariable phrase: String, authentication: Authentication) =
        messageService.getMessagesByPhrase(phrase, authentication.credentials as Long)
}
