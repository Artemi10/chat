package com.devanmejia.chataccount.repository;

import com.devanmejia.chataccount.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT user FROM User user JOIN FETCH user.friends friends " +
            "WHERE user.login = :login")
    Optional<User> findByLoginWithFriends(String login);
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}
