package com.devanmejia.chataccount.service.user;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.transfer.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public interface UserService {
    User findByLogin(String login);
    Set<User> findAllByLogins(Collection<String> logins);
    Set<User> getFriends(String login);
    Set<Chat> getChats(String login);
    void updateUser(User userToUpdate);
    UserInfo save(UserInfo userInfo);
    public boolean isExistsInChat(User user, Long chatId);
}
