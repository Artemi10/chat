package com.devanmejia.chataccount.repository;

import com.devanmejia.chataccount.model.Chat;
import com.devanmejia.chataccount.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "chat_users")
    List<Chat> findAllByAdmin(User admin);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "chat_users")
    Optional<Chat> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM chats_users WHERE user_id = :userId AND chat_id = :chatId", nativeQuery = true)
    void deleteUserFromChat(Long userId, Long chatId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO chats_users (chat_id, user_id) VALUES (:userId, :chatId)", nativeQuery = true)
    void addUserToChat(Long userId, Long chatId);

    @Modifying
    @Transactional
    @Query("UPDATE Chat c SET c.name = :name WHERE c.id = :id")
    void updateChat(Long id, String name);

    boolean existsByName(String name);
    default boolean notExistsByName(String name){
        return !existsByName(name);
    }
}
