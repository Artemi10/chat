package com.devanmejia.chataccount.model;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @NaturalId
    @Column(name = "login")
    private String login;
    @Column(name = "birthdate")
    private Date birthDate;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friends",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "friend_id") })
    private Set<User> friends = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chats_users",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "chat_id") })
    private Set<Chat> chats = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
