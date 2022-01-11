package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.exception.EntityException;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public Chat updateChatUsers(String name, Set<User> usersToDelete, Set<User> usersToAdd) {
        Chat chat = findByName(name);
        Set<User> users = chat.getUsers();
        usersToDelete.forEach(users::remove);
        users.addAll(usersToAdd);
        return chatRepository.save(chat);
    }


    @Override
    public Chat createChat(String name, User admin, Set<User> users) {
        if (chatRepository.notExistsByName(name)){
            users.add(admin);
            Chat chat = new Chat(name, admin, users);
            return chatRepository.save(chat);
        }
        else{
            throw new EntityException(String.format("Chat %s has already been created", name));
        }
    }

    @Override
    public Chat findByName(String name) {
        return chatRepository.findByName(name)
                .orElseThrow(() -> new EntityException(String.format("Chat %s not found", name)));
    }

    @Override
    public Chat updateChatName(long id, String newName) {
       Chat chat = chatRepository.getChatById(id)
               .orElseThrow(() -> new EntityException("Chat not found"));
       if (!chat.getName().equals(newName)){
           if (chatRepository.notExistsByName(newName)){
               chat.setName(newName);
               chatRepository.save(chat);
               return chat;
           }
           else{
               throw new EntityException(String.format("%s has already been created", newName));
           }
       }
       return chat;
    }

    @Override
    public boolean isUserContains(String login, Chat chat) {
        return chat.getAdmin().getLogin().equals(login)
                || chat.getUsers().stream()
                .map(User::getLogin)
                .anyMatch(userLogin -> userLogin.equals(login));
    }

    @Override
    public boolean isUserAdmin(String login, long chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        return chatOptional.map(chat -> chat.getAdmin().getLogin().equals(login)).orElse(false);
    }

    @Override
    public boolean isUserChat(long userId, long chatId) {
        Optional<Chat> optionalChat = chatRepository.getChatById(chatId);
        return optionalChat.map(chat -> chat.getUsers().stream()
                .map(User::getId)
                .anyMatch(id -> id == userId)).orElse(false);
    }

    @Override
    public Set<Chat> getChats(String login, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Chat> chats = chatRepository.findUserChats(login, pageable);
        return new HashSet<>(chats);
    }
}
