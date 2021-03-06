package com.devanmejia.chataccount.model;

import com.devanmejia.chataccount.model.user.User;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "chat_users",
        attributeNodes = @NamedAttributeNode("users"))
public class Chat extends BaseEntity {
    @NaturalId(mutable = true)
    @Column(name = "name", unique = true)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private User admin;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chats_users",
            joinColumns = { @JoinColumn(name = "chat_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") })
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Chat chat = (Chat) o;
        return name.equals(chat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
