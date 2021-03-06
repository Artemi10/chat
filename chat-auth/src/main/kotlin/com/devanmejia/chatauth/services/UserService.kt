package com.devanmejia.chatauth.services

import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.models.UserState
import com.devanmejia.chatauth.repositories.UserRepository
import com.devanmejia.chatauth.transfer.CodeDTO
import com.devanmejia.chatauth.transfer.LogInDTO
import com.devanmejia.chatauth.transfer.SignUpDTO
import org.apache.commons.lang3.RandomStringUtils
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

interface UserService {
    suspend fun logIn(logInDTO: LogInDTO): User
    suspend fun signUp(signUpDTO: SignUpDTO): User
    suspend fun checkSecretCode(codeDTO: CodeDTO): User
    suspend fun getUserByLogin(login: String): User
    suspend fun getChatUser(login: String, chatId: Long): User
    suspend fun getAvailableChatsId(login: String): Set<Long>
}

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    override suspend fun logIn(logInDTO: LogInDTO): User {
        val user = userRepository.getUserInfo(logInDTO.login)
        if (user != null){
            if (passwordEncoder.matches(logInDTO.password, user.password)){
                val secretCode = RandomStringUtils.randomAlphabetic(8)
                user.setNewSecretCode(secretCode)
                return userRepository.saveUserInfo(user)
            }
            else throw AuthException("Incorrect password")
        }
        else throw AuthException("Incorrect login")
    }

    override suspend fun signUp(signUpDTO: SignUpDTO): User {
        if (!existsByLogin(signUpDTO.login)){
            val encodedPassword = passwordEncoder.encode(signUpDTO.password)
            val newUser = User(signUpDTO.login, encodedPassword,
                signUpDTO.birthDate ,signUpDTO.email)
            return userRepository.saveUserInfo(newUser)
        }
        else throw AuthException("${signUpDTO.login} has already been registered")
    }

    override suspend fun checkSecretCode(codeDTO: CodeDTO): User {
        val user = userRepository.getUserInfo(codeDTO.login)
        if (user != null){
            if (user.secretCode == codeDTO.code && user.state == UserState.UNVERIFIED){
                user.discardSecretCode()
                return userRepository.saveUserInfo(user)
            }
            else throw AuthException("Incorrect code")
        }
        else throw AuthException("Incorrect login")
    }

    private suspend fun existsByLogin(login: String) =
        userRepository.getUserInfo(login) != null

    override suspend fun getUserByLogin(login: String): User {
        val user = userRepository.getUserInfo(login)
        if (user != null) return user
        else throw AuthException("Incorrect login")
    }

    override suspend fun getChatUser(login: String, chatId: Long): User {
        val user = userRepository.getChatUserInfo(login, chatId)
        if (user != null) return user
        else throw AuthException("Incorrect login")
    }

    override suspend fun getAvailableChatsId(login: String) =
        userRepository.getUserChatId(login)

}

