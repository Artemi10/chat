package com.devanmejia.chataccount.transfer;

import com.devanmejia.chataccount.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class UserInfo {
    private String login;
    private String password;
    private String email;
    private String secretCode;
    private String state;
    private Date birthDate;
    private boolean enable;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;

    public static UserInfo form(User user) {
        return new UserInfo(user.getLogin(), user.getPassword(),
                user.getEmail(), user.getSecretCode(), user.getState().name(),
                user.getBirthDate(), user.isEnabled(), user.isAccountNonLocked(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired()
        );
    }
}
