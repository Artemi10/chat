package com.devanmejia.chatmessaging.service

import com.devanmejia.chatmessaging.model.Message
import com.devanmejia.chatmessaging.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
interface MessageService {
    suspend fun getNextLatestMessages(amount: Int, skipAmount: Int = 0): List<Message>
    suspend fun getLiveMessageStream(): Flow<Message>
    suspend fun postMessageInLiveStream(messages: Flow<Message>)
    suspend fun getMessagesByPhrase(phrase: String): List<Message>
}

@Service
class MessageServiceImpl(private val messageRepository: MessageRepository) : MessageService {
    val stream: MutableSharedFlow<Message> = MutableSharedFlow()

    override suspend fun getNextLatestMessages(amount: Int, skipAmount: Int): List<Message> {
        val pageRequest = PageRequest.of(skipAmount / amount, amount)
        return messageRepository.getLatestMessages(pageRequest).reversed()
    }

    override suspend fun getLiveMessageStream(): Flow<Message> = stream

    override suspend fun postMessageInLiveStream(messages: Flow<Message>) {
        messages.onEach { stream.emit(it) }
            .let { messageRepository.saveAll(it) }.collect()
    }

    override suspend fun getMessagesByPhrase(phrase: String): List<Message> =
        messageRepository.getMessagesByContentRegex(".*$phrase.*")

}

