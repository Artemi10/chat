package com.devanmejia.chatmessaging.service

import com.devanmejia.chatmessaging.configuration.security.User
import com.devanmejia.chatmessaging.exception.AuthException
import com.devanmejia.chatmessaging.transfer.AuthenticationDTO
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Service
interface CryptoService {
    fun getPublicKey(): String
    fun decryptUserDetails(authenticationDTO: AuthenticationDTO): UserDetails
}

@Service
class CryptoServiceImpl : CryptoService {

    companion object{
        private const val KEY_ALGORITHM = "RSA"
        private const val DATA_ALGORITHM = "AES"
    }

    private val privateKey: PrivateKey
    private val publicKey: PublicKey

    init {
        val keyPair = generateRSAKeyPair()
        privateKey = keyPair.private
        publicKey = keyPair.public
    }

    private fun generateRSAKeyPair(): KeyPair {
        val secureRandom = SecureRandom()
        val keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM)
        keyPairGenerator.initialize(1024, secureRandom)
        return keyPairGenerator.generateKeyPair()
    }

    override fun getPublicKey(): String = DatatypeConverter.printBase64Binary(publicKey.encoded)

    override fun decryptUserDetails(authenticationDTO: AuthenticationDTO): UserDetails {
        val secretKey = decryptKey(authenticationDTO.key)
        val decryptedData = decryptData(authenticationDTO.data, secretKey)
        return deserializeUserDetails(decryptedData)
    }

    private fun decryptKey(encryptedKey: String): SecretKey {
        val decrypt = Cipher.getInstance(KEY_ALGORITHM)
        decrypt.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedData = decrypt.doFinal(DatatypeConverter.parseBase64Binary(encryptedKey))
        return getSecretKey(decryptedData)
    }

    private fun getSecretKey(key: ByteArray): SecretKey {
        return try {
            SecretKeySpec(key, DATA_ALGORITHM)
        } catch (e: Exception) {
            e.printStackTrace()
            throw AuthException("Can not parse key. ${e.message}")
        }
    }

    private fun decryptData(data: String, secretKey: SecretKey): ByteArray{
        val decrypt = Cipher.getInstance(DATA_ALGORITHM)
        decrypt.init(Cipher.DECRYPT_MODE, secretKey)
        return decrypt.doFinal(DatatypeConverter.parseBase64Binary(data))
    }

    private fun deserializeUserDetails(byteArray: ByteArray) =
        jacksonObjectMapper().readValue(String(byteArray), User::class.java)

}
