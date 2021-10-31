package com.devanmejia.chataccount.service.chat;

import com.devanmejia.chataccount.model.Chat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {
    Chat findByName(String name);
    List<Chat> findByAdminLogin(String login);
}
