package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.exception.ConverterException;
import com.devanmejia.chataccount.exception.NotFoundException;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.User;
import com.devanmejia.chataccount.repository.ChatRepository;
import com.devanmejia.chataccount.repository.UserRepository;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Endpoint
public class ChatController {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatController(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatByNameRequest")
    public GetChatByNameResponse getChatByName(@RequestPayload GetChatByNameRequest request) {
        Chat chat = chatRepository.findByName(request.getChatName())
                .orElseThrow(() -> new NotFoundException(String.format("Chat %s not found", request.getChatName())));
        GetChatByNameResponse response = new GetChatByNameResponse();
        response.setChat(convert(chat));
        return response;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatsByAdminNameRequest")
    public GetChatsByAdminNameResponse getChatByName(@RequestPayload GetChatsByAdminNameRequest request) {
        User admin = userRepository.findUserByLogin(request.getAdminName())
                .orElseThrow(() -> new NotFoundException(String.format("User %s not found", request.getAdminName())));
        GetChatsByAdminNameResponse response = new GetChatsByAdminNameResponse();
        List<Chat> chats = chatRepository.findAllByAdmin(admin);
        response.getChats().addAll(convertChats(chats));
        return response;
    }


    private ChatDTO convert(Chat chat){
        ChatDTO chatDTO = new ChatDTO();
        chatDTO.setName(chat.getName());
        chatDTO.setAdmin(convert(chat.getAdmin()));
        chatDTO.getUsers().addAll(convertUsers(chat.getUsers()));
        return chatDTO;
    }

    private List<ChatUserDTO> convertUsers(Collection<User> users){
        return users.stream().map(this::convert)
                .collect(Collectors.toList());
    }

    private List<ChatDTO> convertChats(Collection<Chat> chats){
        return chats.stream().map(this::convert)
                .collect(Collectors.toList());
    }

    private ChatUserDTO convert(User user){
        ChatUserDTO chatUserDTO = new ChatUserDTO();
        chatUserDTO.setLogin(user.getLogin());
        chatUserDTO.setBirthDate(convert(user.getBirthDate()));
        return chatUserDTO;
    }

    private XMLGregorianCalendar convert(Date date){
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try{
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e){
            throw new ConverterException("Invalid date");
        }
    }
}
