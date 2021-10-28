package com.devanmejia.chatemail.services.email

import com.devanmejia.chatemail.models.Email
import org.springframework.stereotype.Service
import javax.mail.Message

@Service("verifyEmailSender")
class VerifyEmailSender : EmailSender() {

    override fun addMessageContent(email: Email, basicMessage: Message) {
        basicMessage.subject = "Verify code"
        basicMessage.setText("Your verify code is ${email.content}")
    }
}
