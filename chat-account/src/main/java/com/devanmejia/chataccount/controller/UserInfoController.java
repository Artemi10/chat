package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.config.security.authentication.AuthService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.BaseEntity;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.service.converter.Converter;
import com.devanmejia.chataccount.service.user.UserService;
import com.devanmejia.chataccount.transfer.UserInfo;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;
import java.util.Set;

@Endpoint
public class UserInfoController {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final AuthService authService;
    private final UserService userService;
    private final Converter<UserInfoDTO, UserInfo> userInfoUserConverter;

    @Autowired
    public UserInfoController(AuthService authService, UserService userService,
                              Converter<UserInfoDTO, UserInfo> userInfoUserConverter) {
        this.authService = authService;
        this.userService = userService;
        this.userInfoUserConverter = userInfoUserConverter;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserInfoRequest")
    public GetUserInfoResponse getUserInfo(@RequestPayload GetUserInfoRequest request) {
        if (authService.hasPermission(State.SERVICE)) {
            String login = request.getLogin();
            UserInfo userInfo = UserInfo.form(userService.findByLogin(login));
            GetUserInfoResponse response = new GetUserInfoResponse();
            response.setUser(userInfoUserConverter.convert(userInfo));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getChatUserInfoRequest")
    public GetChatUserInfoResponse getChatUserInfo(@RequestPayload GetChatUserInfoRequest request) {
        if (authService.hasPermission(State.SERVICE)) {
            User user = userService.findByLogin(request.getLogin());
            UserInfo userInfo = UserInfo.form(user);
            userInfo.setEnable(userService.isExistsInChat(user, request.getChatId()));
            GetChatUserInfoResponse response = new GetChatUserInfoResponse();
            response.setUser(userInfoUserConverter.convert(userInfo));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserChatIdRequest")
    public GetUserChatIdResponse getUserChatId(@RequestPayload GetUserChatIdRequest request) {
        if (authService.hasPermission(State.SERVICE)) {
            GetUserChatIdResponse response = new GetUserChatIdResponse();
            Set<Long> chatIds = userService.getChatIds(request.getLogin());
            response.getChatIds().addAll(chatIds);
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "saveUserInfoRequest")
    public SaveUserInfoResponse saveUserInfo(@RequestPayload SaveUserInfoRequest request) {
        if (authService.hasPermission(State.SERVICE)) {
            UserInfo userInfo = userInfoUserConverter.reconvert(request.getUserInfo());
            UserInfo savedUserInfo = userService.save(userInfo);
            SaveUserInfoResponse response = new SaveUserInfoResponse();
            response.setUserInfo(userInfoUserConverter.convert(savedUserInfo));
            return response;
        }
        throw new AuthException("Not permit");
    }
}
