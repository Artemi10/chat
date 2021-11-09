package com.devanmejia.chataccount.service.converter;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.User;
import io.spring.guides.gs_producing_web_service.ChatDTO;
import io.spring.guides.gs_producing_web_service.ChatUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service("chatConverter")
public class ChatConverter implements Converter<ChatDTO, Chat>{
    private final Converter<ChatUserDTO, User> chatUserConverter;

    @Autowired
    public ChatConverter(Converter<ChatUserDTO, User> chatUserConverter) {
        this.chatUserConverter = chatUserConverter;
    }

    @Override
    public ChatDTO convert(Chat chat) {
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setId(chat.getId());
        chatDTO.setName(chat.getName());
        chatDTO.setAdmin(chatUserConverter.convert(chat.getAdmin()));
        chatDTO.getUsers()
                .addAll(chatUserConverter.convert(chat.getUsers()));
        return chatDTO;
    }

    @Override
    public Chat reconvert(ChatDTO obj) {
        Chat chat = new Chat();
        chat.setId(obj.getId());
        chat.setName(obj.getName());
        chat.setAdmin(chatUserConverter.reconvert(obj.getAdmin()));
        chat.setUsers(new HashSet<>(chatUserConverter.reconvert(obj.getUsers())));
        return chat;
    }
}
