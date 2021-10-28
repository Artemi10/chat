package com.devanmejia.models

import org.hibernate.Hibernate
import org.hibernate.annotations.NaturalId
import java.util.*
import javax.persistence.*

@MappedSuperclass
open class BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null
}

@Entity
data class User(
    @NaturalId
    private val login: String,
    var birthDate: Date,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "friends",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "friend_id")])
    val friends: Set<User>,
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "chats",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "chat_id")])
    val chats: Set<Chat>
) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User
        return login == other.login
    }

    override fun hashCode(): Int = Objects.hash(login);

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(login = $login )"
    }
}

@Entity
data class Chat(
    @NaturalId
    val name: String,
    @ManyToOne
    @JoinColumn(name = "admin_id")
    val admin: User,
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "chats")
    val users: Set<User>
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Chat

        return name == other.name
    }

    override fun hashCode(): Int = Objects.hash(name);

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(name = $name )"
    }
}
