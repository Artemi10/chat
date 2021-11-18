package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.config.security.authentication.AuthService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.service.converter.Converter;
import com.devanmejia.chataccount.service.user.UserService;
import com.devanmejia.chataccount.transfer.UserInfo;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

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
