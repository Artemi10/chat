package com.devanmejia.chataccount.service.user;

import com.devanmejia.chataccount.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public interface UserService {
    User findByLogin(String login);
    Set<User> findAllByLogins(Collection<String> logins);
    Set<User> getFriends(String login);
    void updateUser(User userToUpdate);
    User createNewUser(User userToCreate);
}
