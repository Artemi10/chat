package com.devanmejia.chataccount.repository.chat;

import com.devanmejia.chataccount.models.Chat;
import com.devanmejia.chataccount.models.User;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    void save(Chat chat);
    Optional<Chat> findById(Long id);
    Optional<Chat> findByName(String name);
    Optional<Chat> findByAdmin(User admin);
    List<Chat> findAll();
    List<Chat> findAll(int skip, int limit);
}
