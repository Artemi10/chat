package com.devanmejia.chataccount.controller;

import com.devanmejia.chataccount.config.security.authentication.AuthService;
import com.devanmejia.chataccount.config.security.crypto.CryptoService;
import com.devanmejia.chataccount.exception.AuthException;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.service.user.UserService;
import com.devanmejia.chataccount.transfer.EncryptedObj;
import com.devanmejia.chataccount.transfer.UserInfo;
import io.spring.guides.gs_producing_web_service.GetUserInfoRequest;
import io.spring.guides.gs_producing_web_service.GetUserInfoResponse;
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
    private final CryptoService<UserInfo> cryptoService;

    @Autowired
    public UserInfoController(AuthService authService, UserService userService,
                              CryptoService<UserInfo> cryptoService) {
        this.authService = authService;
        this.userService = userService;
        this.cryptoService = cryptoService;
    }

    @ResponsePayload
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserInfoRequest")
    public GetUserInfoResponse getUserInfo(@RequestPayload GetUserInfoRequest request) {
        if (authService.hasPermission(State.SERVICE)) {
            String login = request.getLogin();
            UserInfo userInfo = UserInfo.form(userService.findByLogin(login));
            EncryptedObj encryptedObj = cryptoService.encryptObj(userInfo, request.getPublicKey());
            GetUserInfoResponse response = new GetUserInfoResponse();
            response.setEncryptedUser(encryptedObj.getEncryptedData());
            response.setEncryptedKey(encryptedObj.getEncryptedKey());
            return response;
        }
        throw new AuthException("Not permit");
    }
}
