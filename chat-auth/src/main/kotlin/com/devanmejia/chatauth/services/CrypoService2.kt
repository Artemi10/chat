package com.devanmejia.chatauth.services


import com.devanmejia.chatauth.exceptions.AuthException
import com.devanmejia.chatauth.models.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import java.security.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter

@Service
interface CryptoService2 {
    fun getPublicKey(): String
    fun decodeUser(encodedUser: String, encodedKey: String): User
}

@Service
class CryptoService2Impl : CryptoService2 {

    companion object{
        private const val KEY_ALGORITHM = "RSA"
        private const val DATA_ALGORITHM = "AES"
    }

    private val publicKey: PublicKey
    private val privateKey: PrivateKey
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

    override fun getPublicKey(): String =
        DatatypeConverter.printBase64Binary(publicKey.encoded)

    override fun decodeUser(encodedUser: String, encodedKey: String): User {
        val secretKey = decodeKey(encodedKey)
        val byteObj = decodeData(encodedUser, secretKey)
        return deserializeUser(byteObj)
    }

    private fun decodeKey(encodedKey: String): SecretKey{
        val decrypt = Cipher.getInstance(KEY_ALGORITHM)
        decrypt.init(Cipher.DECRYPT_MODE, privateKey)
        val decryptedData = decrypt.doFinal(DatatypeConverter.parseBase64Binary(encodedKey))
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

    private fun decodeData(data: String, secretKey: SecretKey): ByteArray{
        val decrypt = Cipher.getInstance(DATA_ALGORITHM)
        decrypt.init(Cipher.DECRYPT_MODE, secretKey)
        return decrypt.doFinal(DatatypeConverter.parseBase64Binary(data))
    }

    private fun deserializeUser(byteArray: ByteArray): User {
        val str = String(byteArray)
        return jacksonObjectMapper().readValue(str, User::class.java)
    }


}
