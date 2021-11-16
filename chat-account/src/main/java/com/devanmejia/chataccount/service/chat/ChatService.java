package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ChatService {
    void updateChat(long id, String newName);
    void deleteUserFromChat(String name, User user);
    void addUserToChat(String name, User user);
    Chat createChat(String name, User admin, Set<User> users);
    Chat findByName(String name);
    List<Chat> findByAdmin(User admin);
    boolean isUserContains(String login, Chat chat);
    boolean isUserAdmin(String login, Chat chat);
}
