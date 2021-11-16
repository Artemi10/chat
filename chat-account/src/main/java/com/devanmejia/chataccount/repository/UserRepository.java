package com.devanmejia.chataccount.repository;

import com.devanmejia.chataccount.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT user FROM User user JOIN FETCH user.friends friends " +
            "WHERE user.login = :login")
    Optional<User> findByLoginWithFriends(String login);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.login = :login, u.birthDate = :birthDate " +
            "WHERE u.id = :id")
    void updateUser(Long id, String login, Date birthDate);
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String login);
}
