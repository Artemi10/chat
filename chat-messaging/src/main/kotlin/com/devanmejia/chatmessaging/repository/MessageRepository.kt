package com.devanmejia.chatmessaging.repository

import com.devanmejia.chatmessaging.model.Message
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface MessageRepository : CoroutineCrudRepository<Message, String> {
    @Query("{}", sort = "{_id : -1}")
    suspend fun getLatestMessages(pageable: PageRequest): List<Message>

    @Query("{ 'content' : { \$regex: ?0, \$options: 'i' } }")
    suspend fun getMessagesByContentRegex(regex: String): List<Message>
}
