package com.devanmejia.chataccount.service.user;

import com.devanmejia.chataccount.exception.EntityException;
import com.devanmejia.chataccount.model.BaseEntity;
import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.State;
import com.devanmejia.chataccount.model.user.User;
import com.devanmejia.chataccount.repository.UserRepository;
import com.devanmejia.chataccount.transfer.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
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
        return logins.stream()
                .filter(login -> !login.isEmpty())
                .map(this::findByLogin)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> findAllByLogins(Collection<String> logins, String adminLogin) {
        return logins.stream()
                .filter(login -> !login.isEmpty() && !login.equals(adminLogin))
                .map(this::findByLogin)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getFriends(String login) {
        Optional<User> optionalUser = userRepository.findUserWithFriends(login);
        if (optionalUser.isPresent()) {
            return optionalUser.get().getFriends();
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public UserInfo save(UserInfo userInfo) {
        Optional<User> userOptional = userRepository.findByLogin(userInfo.getLogin());
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user.setLogin(userInfo.getLogin());
            user.setEmail(userInfo.getEmail());
            user.setPassword(userInfo.getPassword());
            user.setSecretCode(userInfo.getSecretCode());
            user.setState(State.valueOf(userInfo.getState()));
        } else {
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
        if (!userToUpdate.isNew()) {
            if (!userRepository.existsByLogin(login)) {
                userRepository.updateUser(userToUpdate.getId(),
                        userToUpdate.getLogin(), userToUpdate.getBirthDate());
            } else {
                throw new EntityException(String.format("%s has already been registered", login));
            }
        } else {
            throw new EntityException("Can not update user. Id is not specified");
        }
    }

    @Override
    public boolean isExistsInChat(User user, Long chatId) {
        return userRepository.findUserWithChats(user.getLogin())
                .orElseThrow(() -> new EntityException("Can not update user. Id is not specified"))
                .getChats().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .contains(chatId);
    }

    @Override
    public List<User> getUsersStartWith(String searchName, int page, int size, String login) {
        if (!searchName.isEmpty()) {
            String pattern = searchName + "%";
            Pageable pageable = PageRequest.of(page, size);
            return userRepository.getUserLoginsByPattern(pattern, login, pageable);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Set<Long> getChatIds(String login) {
        Optional<User> userOptional = userRepository.findUserWithChats(login);
        return userOptional.map(user -> user.getChats().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())).orElseGet(HashSet::new);
    }
}
