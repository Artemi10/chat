package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ChatService {
    void deleteUserFromChat(String name, User user);
    void addUserToChat(String name, User user);
    Chat createChat(String name, User admin, Set<User> users);
    Chat findByName(String name);
    List<Chat> findByAdminLogin(User admin);
}
