package com.devanmejia.chatauth.controllers

import com.devanmejia.chatauth.services.CryptoService
import com.devanmejia.chatauth.services.email.EmailService
import com.devanmejia.chatauth.services.jwt.JWTSigner
import com.devanmejia.chatauth.services.user.UserService
import com.devanmejia.chatauth.transfer.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api/auth")
class AuthController(private val jwtSigner: JWTSigner,
                     private val userService: UserService,
                     private val emailService: EmailService,
                     private val cryptoService: CryptoService
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

    @PostMapping("/authentication")
    suspend fun getAuthentication(@RequestBody tokeDTO: TokenDTO): AuthenticationDTO {
        val login = jwtSigner.getLogin(tokeDTO.jwt)
        val user = userService.getUserByLogin(login)
        return cryptoService.encryptAuthentication(user, tokeDTO.key)
    }
}
