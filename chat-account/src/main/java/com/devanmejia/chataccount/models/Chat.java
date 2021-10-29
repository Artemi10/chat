package com.devanmejia.chataccount.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "chats")
public class Chat extends BaseEntity {
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private User admin;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "chats")
    private Set<User> users;

    public Chat() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
