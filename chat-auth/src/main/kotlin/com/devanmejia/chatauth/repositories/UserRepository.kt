package com.devanmejia.chatauth.repositories

import com.devanmejia.chatauth.configuration.soap.UserInfoClient
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.services.CryptoService2
import org.springframework.stereotype.Service

@Service
interface UserRepository{
    suspend fun getUserByLogin(login: String): User?
    suspend fun save(user: User): User
}

@Service
class UserRepositoryImpl(
    private val userInfoClient: UserInfoClient,
    private val cryptoService2: CryptoService2
): UserRepository {

    override suspend fun getUserByLogin(login: String): User {
        val (encodedUser, encodedKey) = userInfoClient
            .getUserInfo(login, cryptoService2.getPublicKey())
        return cryptoService2.decodeUser(encodedUser, encodedKey)
    }

    override suspend fun save(user: User): User {
        TODO("Not yet implemented")
    }
}
