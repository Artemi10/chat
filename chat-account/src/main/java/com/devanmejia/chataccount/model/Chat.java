package com.devanmejia.chataccount.model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "chats")
@NoArgsConstructor
@AllArgsConstructor
public class Chat extends BaseEntity {
    @Column(name = "name")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private User admin;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "chats")
    private Set<User> users;
}
