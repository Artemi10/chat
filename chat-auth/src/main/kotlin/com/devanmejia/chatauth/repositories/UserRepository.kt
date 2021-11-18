package com.devanmejia.chatauth.repositories

import com.devanmejia.chatauth.configuration.soap.UserInfoClient
import com.devanmejia.chatauth.models.User
import org.springframework.stereotype.Service

@Service
interface UserRepository{
    suspend fun getUserByLogin(login: String): User?
    suspend fun save(user: User): User
}

@Service
class UserRepositoryImpl(private val userInfoClient: UserInfoClient): UserRepository {

    override suspend fun getUserByLogin(login: String) = userInfoClient.getUserInfo(login)

    override suspend fun save(user: User) = userInfoClient.saveUserInfo(user)
}
