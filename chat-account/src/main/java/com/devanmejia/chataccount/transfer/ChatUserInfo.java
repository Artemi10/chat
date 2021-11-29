package com.devanmejia.chataccount.transfer;

import com.devanmejia.chataccount.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class ChatUserInfo {
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
    private boolean isChatMember;

    public static ChatUserInfo form(User user, boolean isChatMember) {
        return new ChatUserInfo(user.getLogin(), user.getPassword(),
                user.getEmail(), user.getSecretCode(), user.getState().name(),
                user.getBirthDate(), user.isEnabled(), user.isAccountNonLocked(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(), isChatMember
        );
    }
}
