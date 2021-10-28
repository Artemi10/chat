package com.devanmejia.chatemail.controller

import com.devanmejia.chatemail.models.Email
import com.devanmejia.chatemail.services.email.EmailSender
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/email")
class EmailController (@Qualifier("verifyEmailSender")
                       private val emailSender: EmailSender
){

    @PostMapping("/verify")
    suspend fun sendMessage(@RequestBody email: Email) = emailSender.sendMessage(email)

}
