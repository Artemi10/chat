package com.devanmejia.chataccount.service.user;

import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.transfer.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public interface UserService {
    User findByLogin(String login);
    Set<User> findAllByLogins(Collection<String> logins);
    Set<Long> getChatIds(String login);
    Set<User> getFriends(String login);
    void updateUser(User userToUpdate);
    UserInfo save(UserInfo userInfo);
    boolean isExistsInChat(User user, Long chatId);
    List<User> getUsersStartWith(String searchName, int page, int size, String login);
}
