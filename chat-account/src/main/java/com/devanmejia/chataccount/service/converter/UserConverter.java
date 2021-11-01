package com.devanmejia.chataccount.service.converter;

import com.devanmejia.chataccount.model.User;
import io.spring.guides.gs_producing_web_service.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

@Service("userConverter")
public class UserConverter implements Converter<UserDTO, User> {
    private final Converter<XMLGregorianCalendar, Date> dateConverter;

    @Autowired
    public UserConverter(Converter<XMLGregorianCalendar, Date> dateConverter) {
        this.dateConverter = dateConverter;
    }

    @Override
    public UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(user.getLogin());
        userDTO.setBirthDate(dateConverter.convert(user.getBirthDate()));
        return userDTO;
    }
}
