package com.devanmejia.chatauth.controllers

import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.services.EmailService
import com.devanmejia.chatauth.services.JWTSigner
import com.devanmejia.chatauth.services.UserService
import com.devanmejia.chatauth.transfer.*
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val jwtSigner: JWTSigner,
    private val userService: UserService,
    private val emailService: EmailService
) {

    @PostMapping("/logIn")
    suspend fun logIn(@RequestBody logInDTO: LogInDTO): String {
        val user = userService.logIn(logInDTO)
        val token = jwtSigner.createJWT(user)
        emailService.sendVerifyEmail(user, token)
        return token
    }

    @PostMapping("/signUp")
    suspend fun signUp(@RequestBody signUpDTO: SignUpDTO): String {
        val user = userService.signUp(signUpDTO)
        return jwtSigner.createJWT(user)
    }

    @GetMapping("/code")
    suspend fun repeatCode(exchange: ServerWebExchange): String{
        val userLogin = jwtSigner.getLogin(exchange)
        val user = userService.getUserByLogin(userLogin)
        val token = jwtSigner.createJWT(user)
        emailService.sendVerifyEmail(user, token)
        return token
    }

    @PostMapping("/verify")
    suspend fun verify(@RequestBody codeDTO: CodeDTO): String {
        val user = userService.checkSecretCode(codeDTO)
        return jwtSigner.createJWT(user)
    }

    @GetMapping("/authentication")
    suspend fun getAuthentication(exchange: ServerWebExchange): UserDetails {
        val login = jwtSigner.getLogin(exchange)
        return  userService.getUserByLogin(login)
    }

    @PostMapping("/authentication")
    suspend fun getChatAuthentication(exchange: ServerWebExchange, @RequestBody chatId: Long): User {
        val login = jwtSigner.getLogin(exchange)
        return userService.getChatUser(login, chatId)
    }

    @GetMapping("/authentication/chats")
    suspend fun getAvailableChatsId(exchange: ServerWebExchange): Set<Long> {
        val login = jwtSigner.getLogin(exchange)
        return userService.getAvailableChatsId(login)
    }
}
