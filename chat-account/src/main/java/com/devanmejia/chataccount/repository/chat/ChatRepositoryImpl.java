package com.devanmejia.chataccount.repository.chat;

import com.devanmejia.chataccount.models.Chat;
import com.devanmejia.chataccount.models.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.List;
import java.util.Optional;

@Stateless
public class ChatRepositoryImpl implements ChatRepository{
    private final EntityManager entityManager;

    @Inject
    public ChatRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Chat chat) {
        entityManager.persist(chat);
    }

    @Override
    public Optional<Chat> findById(Long id) {
        Chat chatCandidate = entityManager.find(Chat.class, id);
        return Optional.ofNullable(chatCandidate);
    }

    @Override
    public Optional<Chat> findByName(String name) {
        return Optional.ofNullable(entityManager
                .createQuery("SELECT chat FROM Chat chat WHERE chat.name = :name", Chat.class)
                .setParameter("name", name)
                .getSingleResult());
    }

    @Override
    public Optional<Chat> findByAdmin(User admin) {
        return Optional.ofNullable(entityManager
                .createQuery("SELECT chat FROM Chat chat WHERE chat.admin = :admin", Chat.class)
                .setParameter("admin", admin)
                .getSingleResult());
    }

    @Override
    public List<Chat> findAll() {
        return entityManager
                .createQuery("SELECT chat FROM Chat chat", Chat.class)
                .getResultList();
    }

    @Override
    public List<Chat> findAll(int skip, int limit) {
        return entityManager
                .createQuery("SELECT chat FROM Chat chat", Chat.class)
                .setFirstResult(skip)
                .setMaxResults(limit)
                .getResultList();
    }
}
