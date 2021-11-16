package com.devanmejia.chatauth.controllers

import com.devanmejia.chatauth.repositories.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class TestController(
    private val userRepository: UserRepository
) {

    @GetMapping("/test")
    suspend fun logIn() = userRepository.getUserByLogin("devanmejia")

}
