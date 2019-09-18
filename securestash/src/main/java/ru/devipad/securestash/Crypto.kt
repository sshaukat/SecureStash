package ru.devipad.securestash

import android.util.Base64
import android.util.Base64.NO_WRAP
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.Cipher.DECRYPT_MODE
import javax.crypto.Cipher.ENCRYPT_MODE
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


class Crypto(key: String) {

    companion object {
        private const val UTF8 = "UTF-8"
        private const val KEY_GENERATOR_ALGORITHM = "AES"
        private const val ALGORITHM_PATTERN_COMPLEMENT = "AES/ECB/PKCS5Padding"
        private const val SECURE_ALGORITHM = "SHA1PRNG"
        private const val KEY_LENGTH = 128

        private var instance: Crypto? = null

        fun getInstance(key: String): Crypto {
            if (instance == null)
                instance = Crypto(key)

            return instance!!
        }
    }

    private val secretKeySpec: SecretKeySpec


    init {
        if (key.isNullOrEmpty())
            throw EncryptException("Key should not be empty")

        secretKeySpec = generateSecretKeySpec(key)
    }

    private fun generateSecretKeySpec(key: String): SecretKeySpec {
        val keyGenerator: KeyGenerator
        val secureRandom: SecureRandom
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_GENERATOR_ALGORITHM)
            secureRandom = SecureRandom.getInstance(SECURE_ALGORITHM)
            secureRandom.setSeed(key.toByteArray(charset(UTF8)))
        } catch (ignored: Exception) {
            throw EncryptException(ignored.message, ignored)
        }

        keyGenerator.init(KEY_LENGTH, secureRandom)
        val secretKey = keyGenerator.generateKey()
        val encoded = secretKey.encoded
        return SecretKeySpec(encoded, KEY_GENERATOR_ALGORITHM)
    }

    fun encrypt(content: String): String {
        val cipher = Cipher.getInstance(ALGORITHM_PATTERN_COMPLEMENT)
        cipher.init(ENCRYPT_MODE, secretKeySpec)
        val bytes = cipher.doFinal(content.toByteArray(charset(UTF8)))
        return Base64.encodeToString(bytes, NO_WRAP)
    }

    fun decrypt(content: String): String {
        val cipher = Cipher.getInstance(ALGORITHM_PATTERN_COMPLEMENT)
        cipher.init(DECRYPT_MODE, secretKeySpec)
        val decode = Base64.decode(content, NO_WRAP)
        val original = cipher.doFinal(decode)
        return original.toString(charset(UTF8))
    }

}