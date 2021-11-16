package com.devanmejia.chataccount.transfer;

import com.devanmejia.chataccount.model.user.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserInfo {
    private String login;
    private String password;
    private String email;
    private String secretCode;
    private String state;
    private boolean enable;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;

    public static UserInfo form(User user){
        return new UserInfo(user.getLogin(), user.getPassword(),
                user.getEmail(), user.getSecretCode(), user.getState().name(),
                user.isEnabled(), user.isAccountNonLocked(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired()
        );
    }

}
