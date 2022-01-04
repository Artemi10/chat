package com.devanmejia.chataccount.repository;

import com.devanmejia.chataccount.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT user FROM User user " +
            "JOIN FETCH user.friends WHERE user.login = :login")
    Optional<User> findUserWithFriends(String login);

    @Query("SELECT user FROM User user JOIN FETCH user.chats chats " +
            "JOIN FETCH chats.users WHERE user.login = :login")
    Optional<User> findUserWithChats(String login);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.login = :login, u.birthDate = :birthDate " +
            "WHERE u.id = :id")
    void updateUser(Long id, String login, Date birthDate);

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("SELECT user FROM User user " +
            "WHERE user.login LIKE :pattern AND user.state <> 'SERVICE' AND user.login <> :userLogin " +
            "ORDER BY user.id")
    List<User> getUserLoginsByPattern(String pattern, String userLogin, Pageable pageable);
}
