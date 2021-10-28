package com.devanmejia.chatemail.services.email

import com.devanmejia.chatemail.models.Email
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Service
abstract class EmailSender {

    companion object{
        private val emailPropertiesConfig: Properties
        private val emailProperties: Properties

        init {
            emailProperties = loadProperties("email-properties/email.properties")
            emailPropertiesConfig = loadProperties("email-properties/emailConfiguration.properties")
        }

        private fun loadProperties(propertiesPath: String): Properties{
            val properties = Properties()
            val inStreamEmailPropertiesConfig = ClassPathResource(propertiesPath).inputStream
            properties.load(inStreamEmailPropertiesConfig)
            return properties
        }
    }

    suspend fun sendMessage(email: Email){
        val message = createMessage(email)
        addMessageContent(email, message)
        Transport.send(message)
    }

    protected abstract fun addMessageContent(email: Email, basicMessage: Message)

    private fun createMessage(email: Email): Message{
        val message = MimeMessage(createSession())
        message.setFrom(InternetAddress(emailProperties.getProperty("emailAddress")))
        message.setRecipient(Message.RecipientType.TO, InternetAddress(email.address))
        return message
    }

    private fun createSession(): Session {
        return Session.getDefaultInstance(emailPropertiesConfig, object :Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication {
                val address = emailProperties.getProperty("emailAddress")
                val password = emailProperties.getProperty("emailPassword")
                return PasswordAuthentication(address, password)
            }
        })
    }
}
