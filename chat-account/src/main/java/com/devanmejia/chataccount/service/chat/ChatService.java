package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public interface ChatService {
    Chat updateChatName(long id, String newName);
    @Transactional
    Chat updateChatUsers(String name, Set<User> usersToDelete, Set<User> usersToAdd);
    Chat createChat(String name, User admin, Set<User> users);
    Chat findByName(String name);
    boolean isUserContains(String login, Chat chat);
    boolean isUserAdmin(String login, long chatId);
    boolean isUserChat(long userId, long chatId);
    Set<Chat> getChats(String login, int page, int size);
}
