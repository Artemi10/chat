package com.devanmejia.chataccount.controller;


import com.devanmejia.chataccount.config.security.authentication.AuthService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.service.chat.ChatService;
import com.devanmejia.chataccount.service.converter.Converter;
import com.devanmejia.chataccount.service.user.UserService;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Set;

@Endpoint
public class ChatController {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final ChatService chatService;
    private final UserService userService;
    private final Converter<ChatDTO, Chat> chatConverter;
    private final AuthService authService;

    @Autowired
    public ChatController(ChatService chatService, UserService userService,
                          Converter<ChatDTO, Chat> chatConverter, AuthService authService) {
        this.chatService = chatService;
        this.userService = userService;
        this.chatConverter = chatConverter;
        this.authService = authService;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatByNameRequest")
    public GetChatByNameResponse getChatByName(@RequestPayload GetChatByNameRequest request) {
        if (authService.hasPermission(State.ACTIVE)){
            String login = authService.getUserName();
            Chat chat = chatService.findByName(request.getChatName());
            if (chatService.isUserContains(login, chat)){
                GetChatByNameResponse response = new GetChatByNameResponse();
                response.setChat(chatConverter.convert(chat));
                return response;
            }
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createChatRequest")
    public CreateChatResponse createChat(@RequestPayload CreateChatRequest request) {
        if (authService.hasPermission(State.ACTIVE)){
            User admin = userService.findByLogin(authService.getUserName());
            Set<User> users = userService.findAllByLogins(request.getUserNames());
            Chat chat = chatService.createChat(request.getChatName(), admin, users);
            CreateChatResponse response = new CreateChatResponse();
            response.setChat(chatConverter.convert(chat));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserChatsRequest")
    public GetUserChatsResponse getUserChats(@RequestPayload GetUserChatsRequest request) {
        if (authService.hasPermission(State.ACTIVE)){
            String login = authService.getUserName();
            Set<Chat> chats = chatService.getChats(login, request.getPage(), request.getSize());
            GetUserChatsResponse response = new GetUserChatsResponse();
            response.getChats().addAll(chatConverter.convert(chats));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateChatNameRequest")
    public UpdateChatNameResponse updateChatName(@RequestPayload UpdateChatNameRequest request) {
        if (authService.hasPermission(State.ACTIVE)) {
            String chatName = request.getNewChatName();
            Chat updatedChat = chatService.updateChatName(request.getId(), chatName);
            UpdateChatNameResponse response = new UpdateChatNameResponse();
            response.setChat(chatConverter.convert(updatedChat));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "isUserChatIdRequest")
    public IsUserChatResponse isUserChatId(@RequestPayload IsUserChatRequest request) {
        if (authService.hasPermission(State.ACTIVE)) {
            boolean isUserChat = chatService
                    .isUserChat(request.getUserId(), request.getChatId());
            IsUserChatResponse response = new IsUserChatResponse();
            response.setIsUserChat(isUserChat);
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @Transactional
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateChatRequest")
    public UpdateChatResponse updateChat(@RequestPayload UpdateChatRequest request){
        String login = authService.getUserName();
        if (authService.hasPermission(State.ACTIVE)
                && chatService.isUserAdmin(login, request.getChatId())) {
            String chatName = request.getNewChatName();
            chatService.updateChatName(request.getChatId(), chatName);
            Set<User> usersToAdd = userService.findAllByLogins(request.getUserLoginsToAdd(), login);
            Set<User> usersToDelete = userService.findAllByLogins(request.getUserLoginsToDelete(), login);
            Chat updatedChat = chatService.updateChatUsers(chatName, usersToDelete, usersToAdd);
            UpdateChatResponse response = new UpdateChatResponse();
            response.setChat(chatConverter.convert(updatedChat));
            return response;
        }
        throw new AuthException("Not permit");
    }
}
