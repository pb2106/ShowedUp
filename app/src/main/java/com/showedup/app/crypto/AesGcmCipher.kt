package com.showedup.app.crypto

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AES-256-GCM encryption/decryption.
 * Key derived via PBKDF2-HMAC-SHA256 with 310,000 iterations per OWASP recommendations.
 */
@Singleton
class AesGcmCipher @Inject constructor() {

    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val KEY_LENGTH = 256
        private const val IV_LENGTH = 12 // 96 bits
        private const val TAG_LENGTH = 128 // 128 bits
        private const val SALT_LENGTH = 32
        private const val ITERATIONS = 310_000
        private const val KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256"
    }

    private val secureRandom = SecureRandom()

    /**
     * Derives AES-256 key from passphrase using PBKDF2.
     */
    fun deriveKey(passphrase: CharArray, salt: ByteArray): SecretKeySpec {
        val spec = PBEKeySpec(passphrase, salt, ITERATIONS, KEY_LENGTH)
        val factory = SecretKeyFactory.getInstance(KEY_FACTORY_ALGORITHM)
        val keyBytes = factory.generateSecret(spec).encoded
        spec.clearPassword()
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Generates a cryptographically secure random salt.
     */
    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        secureRandom.nextBytes(salt)
        return salt
    }

    /**
     * Encrypts plaintext with AES-256-GCM.
     * @return [salt (32)] + [iv (12)] + [ciphertext + auth tag]
     */
    fun encrypt(plaintext: ByteArray, passphrase: CharArray): ByteArray {
        val salt = generateSalt()
        val key = deriveKey(passphrase, salt)
        val iv = ByteArray(IV_LENGTH).also { secureRandom.nextBytes(it) }

        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, iv))
        val ciphertext = cipher.doFinal(plaintext)

        return salt + iv + ciphertext
    }

    /**
     * Decrypts ciphertext produced by [encrypt].
     * @param data [salt (32)] + [iv (12)] + [ciphertext + auth tag]
     */
    fun decrypt(data: ByteArray, passphrase: CharArray): ByteArray {
        val salt = data.copyOfRange(0, SALT_LENGTH)
        val iv = data.copyOfRange(SALT_LENGTH, SALT_LENGTH + IV_LENGTH)
        val ciphertext = data.copyOfRange(SALT_LENGTH + IV_LENGTH, data.size)

        val key = deriveKey(passphrase, salt)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(TAG_LENGTH, iv))
        return cipher.doFinal(ciphertext)
    }
}
