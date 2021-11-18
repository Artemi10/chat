package com.devanmejia.chataccount.service.user;

import com.devanmejia.chataccount.exception.EntityException;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.repository.UserRepository;
import com.devanmejia.chataccount.transfer.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
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
    public UserInfo save(UserInfo userInfo) {
        Optional<User> userOptional = userRepository.findByLogin(userInfo.getLogin());
        User user;
        if (userOptional.isPresent()){
            user = userOptional.get();
            user.setLogin(userInfo.getLogin());
            user.setEmail(userInfo.getEmail());
            user.setPassword(userInfo.getPassword());
            user.setSecretCode(userInfo.getSecretCode());
            user.setState(State.valueOf(userInfo.getState()));
        }
        else {
            user = new User(userInfo.getLogin(), userInfo.getBirthDate(),
                    userInfo.getEmail(), userInfo.getPassword(), userInfo.getSecretCode(),
                    State.valueOf(userInfo.getState()), new HashSet<>(), new HashSet<>());
        }
        userRepository.save(user);
        return UserInfo.form(user);
    }

    @Override
    public void updateUser(User userToUpdate) {
        String login = userToUpdate.getLogin();
        if (!userToUpdate.isNew()){
            if (!userRepository.existsByLogin(login)){
                userRepository.updateUser(userToUpdate.getId(),
                        userToUpdate.getLogin(), userToUpdate.getBirthDate());
            }
            else {
                throw new EntityException(String.format("%s has already been registered", login));
            }
        }
        else {
            throw new EntityException("Can not update user. Id is not specified");
        }
    }
}
