package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.service.chat.ChatService;
import com.devanmejia.chataccount.service.converter.Converter;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class ChatController {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final ChatService chatService;
    private final Converter<ChatDTO, Chat> chatConverter;

    @Autowired
    public ChatController(ChatService chatService, Converter<ChatDTO, Chat> chatConverter) {
        this.chatService = chatService;
        this.chatConverter = chatConverter;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatByNameRequest")
    public GetChatByNameResponse getChatByName(@RequestPayload GetChatByNameRequest request) {
        Chat chat = chatService.findByName(request.getChatName());
        GetChatByNameResponse response = new GetChatByNameResponse();
        response.setChat(chatConverter.convert(chat));
        return response;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatsByAdminNameRequest")
    public GetChatsByAdminNameResponse getChatsByAdminName(@RequestPayload GetChatsByAdminNameRequest request) {
        List<Chat> chats = chatService.findByAdminLogin(request.getAdminName());
        GetChatsByAdminNameResponse response = new GetChatsByAdminNameResponse();
        response.getChats().addAll(chatConverter.convert(chats));
        return response;
    }
}
