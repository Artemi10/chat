package com.devanmejia.chatmessaging.controller

import com.devanmejia.chatmessaging.model.Message
import com.devanmejia.chatmessaging.service.MessageService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/message")
class MessageController(private val messageService: MessageService) {
    private val messageAmountPerPage = 10

    @GetMapping
    suspend fun getLatestMessages(): List<Message> =
        messageService.getNextLatestMessages(messageAmountPerPage)

    @GetMapping("/skip/{skipAmount}")
    suspend fun getNextLatestMessages(@PathVariable skipAmount: Int): List<Message> =
        messageService.getNextLatestMessages(messageAmountPerPage, skipAmount)

    @GetMapping("/find/{phrase}")
    suspend fun getMessagesByPhrase(@PathVariable phrase: String): List<Message> =
        messageService.getMessagesByPhrase(phrase)
}
