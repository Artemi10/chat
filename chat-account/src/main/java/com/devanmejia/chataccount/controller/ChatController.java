package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.config.security.auth_users.AuthUserState;
import com.devanmejia.chataccount.config.security.authentication.AuthService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.User;
import com.devanmejia.chataccount.service.chat.ChatService;
import com.devanmejia.chataccount.service.converter.Converter;
import com.devanmejia.chataccount.service.user.UserService;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
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
        if (authService.hasPermission(AuthUserState.ACTIVE)){
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
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatsByAdminNameRequest")
    public GetChatsByAdminNameResponse getChatsByAdminName(@RequestPayload GetChatsByAdminNameRequest request) {
        if (authService.hasPermission(AuthUserState.ACTIVE)){
            User admin = userService.findByLogin(authService.getUserName());
            List<Chat> chats = chatService.findByAdmin(admin);
            GetChatsByAdminNameResponse response = new GetChatsByAdminNameResponse();
            response.getChats().addAll(chatConverter.convert(chats));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createChatRequest")
    public CreateChatResponse createChat(@RequestPayload CreateChatRequest request) {
        User admin = userService.findByLogin(request.getAdminName());
        Set<User> users = userService.findAllByLogins(request.getUserNames());
        Chat chat = chatService.createChat(request.getChatName(), admin, users);
        CreateChatResponse response = new CreateChatResponse();
        response.setChat(chatConverter.convert(chat));
        return response;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserFromChatRequest")
    public DeleteUserFromChatResponse deleteUserFromChat(@RequestPayload DeleteUserFromChatRequest request) {
       User userToDelete = userService.findByLogin(request.getUserName());
       chatService.deleteUserFromChat(request.getChatName(), userToDelete);
       return new DeleteUserFromChatResponse();
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addUserToChatRequest")
    public AddUserToChatResponse addUserToChat(@RequestPayload AddUserToChatRequest request) {
        User userToAdd = userService.findByLogin(request.getUserName());
        chatService.addUserToChat(request.getChatName(), userToAdd);
        return new AddUserToChatResponse();
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateChatNameRequest")
    public UpdateChatNameResponse updateChat(@RequestPayload UpdateChatNameRequest request) {
        chatService.updateChat(request.getId(), request.getNewChatName());
        return new UpdateChatNameResponse();
    }
}
