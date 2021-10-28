package com.devanmejia.chatauth.repositories

import com.devanmejia.chatauth.models.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository : CoroutineCrudRepository<User, String>{
    suspend fun getUserByLogin(login: String): User?
}
