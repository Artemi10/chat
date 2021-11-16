package com.devanmejia.chatauth.services

import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.models.User
import com.devanmejia.chatauth.transfer.AuthenticationDTO
import com.devanmejia.chatauth.transfer.UserDTO
import org.springframework.stereotype.Service
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.xml.bind.DatatypeConverter

@Service
interface CryptoService {
    fun encryptAuthentication(user: User, keyStr: String): AuthenticationDTO
}

@Service
class CryptoServiceImpl : CryptoService {

    companion object{
        private const val KEY_ALGORITHM = "RSA"
        private const val DATA_ALGORITHM = "AES"
    }

    override fun encryptAuthentication(user: User, keyStr: String): AuthenticationDTO {
        val keyEAS = getAESKey()
        val encryptedData = encryptData(UserDTO(user).toJSON().toByteArray(), keyEAS)
        val encryptedKey = encryptKey(keyEAS.encoded, getPublicKey(keyStr))
        return AuthenticationDTO(encryptedData, encryptedKey)
    }

    private fun encryptData(input: ByteArray, key: SecretKey): String{
        val cipher = Cipher.getInstance(DATA_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return DatatypeConverter.printBase64Binary(cipher.doFinal(input))
    }

    private fun getAESKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(DATA_ALGORITHM)
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    private fun encryptKey(key: ByteArray, publicKey: PublicKey): String{
        val cipher = Cipher.getInstance(KEY_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        return DatatypeConverter.printBase64Binary(cipher.doFinal(key))
    }

    private fun getPublicKey(keyStr: String): PublicKey {
        val encoded: ByteArray = DatatypeConverter.parseBase64Binary(keyStr)
        val keySpec = X509EncodedKeySpec(encoded)
        return try {
            val keyFactory = KeyFactory.getInstance(KEY_ALGORITHM)
            keyFactory.generatePublic(keySpec)
        } catch (e: Exception) {
            e.printStackTrace()
            throw AuthException("Can not parse key. ${e.message}")
        }
    }
}
