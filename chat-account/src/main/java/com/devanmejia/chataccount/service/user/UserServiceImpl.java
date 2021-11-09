package com.devanmejia.chataccount.service.user;

import com.devanmejia.chataccount.exception.EntityException;
import com.devanmejia.chataccount.model.User;
import com.devanmejia.chataccount.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityException(String.format("User %s not found", login)));
    }

    @Override
    public Set<User> findAllByLogins(Collection<String> logins) {
        return logins.stream().map(this::findByLogin).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getFriends(String login) {
        return userRepository.findByLoginWithFriends(login)
                .orElseThrow(() -> new EntityException(String.format("User %s not found", login)))
                .getFriends();
    }

    @Override
    public User createNewUser(User userToCreate) {
        String login = userToCreate.getLogin();
        if (!userRepository.existsByLogin(login)){
            return userRepository.save(userToCreate);
        }
        else {
            throw new EntityException(String.format("%s has already been registered", login));
        }
    }
}
