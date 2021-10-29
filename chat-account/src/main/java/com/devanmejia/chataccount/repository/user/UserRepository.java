package com.devanmejia.chataccount.repository.user;

import com.devanmejia.chataccount.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    List<User> findAll(long skip, long limit);
}
