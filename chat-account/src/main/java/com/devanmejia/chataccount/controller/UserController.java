package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.config.security.authentication.AuthService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.model.user.User;
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
public class UserController {
    private static final String NAMESPACE_URI = "http://spring.io/guides/gs-producing-web-service";
    private final UserService userService;
    private final AuthService authService;
    private final Converter<UserDTO, User> userConverter;

    @Autowired
    public UserController(UserService userService, AuthService authService,
                          Converter<UserDTO, User> userConverter) {
        this.userService = userService;
        this.authService = authService;
        this.userConverter = userConverter;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserFriendsRequest")
    public GetUserFriendsResponse getUserFriends() {
        if (authService.hasPermission(State.ACTIVE)){
            Set<User> friends = userService.getFriends(authService.getUserName());
            GetUserFriendsResponse response = new GetUserFriendsResponse();
            response.getFriends().addAll(userConverter.convert(friends));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserByLoginRequest")
    public GetUserByLoginResponse getUserByLogin() {
        if (authService.hasPermission(State.ACTIVE)) {
            User user = userService.findByLogin(authService.getUserName());
            GetUserByLoginResponse response = new GetUserByLoginResponse();
            response.setUser(userConverter.convert(user));
            return response;
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
    public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        if (authService.hasPermission(State.ACTIVE)){
            User user = userService.findByLogin(authService.getUserName());
            User userToUpdate = userConverter.reconvert(request.getUserToUpdate());
            if (user.getId().equals(userToUpdate.getId())){
                UpdateUserResponse response = new UpdateUserResponse();
                userService.updateUser(userToUpdate);
                return response;
            }
        }
        throw new AuthException("Not permit");
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserLoginsByPatternRequest")
    public GetUserLoginsByPatternResponse getUserLoginsByPattern(@RequestPayload GetUserLoginsByPatternRequest request){
        if (authService.hasPermission(State.ACTIVE)){
            GetUserLoginsByPatternResponse response = new GetUserLoginsByPatternResponse();
            List<User> users = userService
                    .getUsersStartWith(request.getSearchName(), request.getPage(),
                            request.getSize(), authService.getUserName());
            response.getUsers().addAll(userConverter.convert(users));
            return response;
         }
        throw new AuthException("Not permit");
    }
}
