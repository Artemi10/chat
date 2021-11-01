package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.model.User;
import com.devanmejia.chataccount.service.converter.Converter;
import com.devanmejia.chataccount.service.user.UserService;
import io.spring.guides.gs_producing_web_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Set;

@Endpoint
public class UserController {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final UserService userService;
    private final Converter<UserDTO, User> userConverter;

    @Autowired
    public UserController(UserService userService, Converter<UserDTO, User> userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserFriendsRequest")
    public GetUserFriendsResponse getUserFriends(@RequestPayload GetUserFriendsRequest request) {
        Set<User> friends = userService.getFriends(request.getLogin());
        GetUserFriendsResponse response = new GetUserFriendsResponse();
        response.getFriends().addAll(userConverter.convert(friends));
        return response;
    }
}
