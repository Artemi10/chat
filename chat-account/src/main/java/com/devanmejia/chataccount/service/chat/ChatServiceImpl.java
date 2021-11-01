package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.exception.EntityException;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.User;
import com.devanmejia.chataccount.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ChatServiceImpl implements ChatService{
    private final ChatRepository chatRepository;

    @Autowired
    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void deleteUserFromChat(String name, User userToDelete) {
        Chat chat = findByName(name);
        if (!userToDelete.equals(chat.getAdmin())){
            chatRepository.deleteUserFromChat(userToDelete.getId(), chat.getId());
        }
        else {
            throw new EntityException("Can not remove admin from chat");
        }
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
    public List<Chat> findByAdminLogin(User admin) {
        return chatRepository.findAllByAdmin(admin);
    }
}
