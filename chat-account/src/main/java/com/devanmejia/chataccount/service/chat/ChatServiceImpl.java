package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.exception.NotFoundException;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat findByName(String name) {
        return chatRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(String.format("Chat %s not found", name)));
    }

    @Override
    public List<Chat> findByAdminLogin(String login) {
        return chatRepository.findAllByAdminLogin(login);
    }
}
