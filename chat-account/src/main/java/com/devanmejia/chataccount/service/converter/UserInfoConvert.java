package com.devanmejia.chataccount.service.converter;

import com.devanmejia.chataccount.transfer.UserInfo;
import io.spring.guides.gs_producing_web_service.State;
import io.spring.guides.gs_producing_web_service.UserInfoDTO;
import org.springframework.stereotype.Service;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;

@Service
public class UserInfoConvert implements Converter<UserInfoDTO, UserInfo>{
    private final Converter<XMLGregorianCalendar, Date> converter;

    public UserInfoConvert(Converter<XMLGregorianCalendar, Date> converter) {
        this.converter = converter;
    }

    @Override
    public UserInfoDTO convert(UserInfo obj) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setAccountNonExpired(obj.isAccountNonExpired());
        userInfoDTO.setAccountNonLocked(obj.isAccountNonLocked());
        userInfoDTO.setEmail(obj.getEmail());
        userInfoDTO.setEnable(obj.isEnable());
        userInfoDTO.setLogin(obj.getLogin());
        userInfoDTO.setPassword(obj.getPassword());
        userInfoDTO.setSecretCode(obj.getSecretCode());
        userInfoDTO.setBirthDate(converter.convert(obj.getBirthDate()));
        userInfoDTO.setCredentialsNonExpired(obj.isCredentialsNonExpired());
        userInfoDTO.setState(State.valueOf(obj.getState()));
        return userInfoDTO;
    }

    @Override
    public UserInfo reconvert(UserInfoDTO obj) {
        return new UserInfo(obj.getLogin(), obj.getPassword(),
                obj.getEmail(), obj.getSecretCode(),
                obj.getState().name(), converter.reconvert(obj.getBirthDate()),
                obj.isEnable(), obj.isAccountNonLocked(),
                obj.isAccountNonExpired(), obj.isCredentialsNonExpired());
    }
}
