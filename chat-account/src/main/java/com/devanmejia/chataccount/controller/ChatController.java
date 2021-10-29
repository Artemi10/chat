package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.repository.chat.ChatRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/chat")
public class ChatController {
    private final ChatRepository chatRepository;

    @Inject
    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }
}
