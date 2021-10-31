package com.devanmejia.chataccount.service.converter;

import com.devanmejia.chataccount.model.User;
import io.spring.guides.gs_producing_web_service.ChatUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

@Service("chatUserConverter")
public class ChatUserConverter implements Converter<ChatUserDTO, User> {
    private final Converter<XMLGregorianCalendar, Date> dateConverter;

    @Autowired
    public ChatUserConverter(Converter<XMLGregorianCalendar, Date> dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public ChatUserDTO convert(User user) {
        ChatUserDTO chatUserDTO = new ChatUserDTO();
        chatUserDTO.setLogin(user.getLogin());
        chatUserDTO.setBirthDate(dateConverter.convert(user.getBirthDate()));
        return chatUserDTO;
    }
}
