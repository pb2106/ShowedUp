package com.showedup.app.crypto

import java.security.MessageDigest
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SHA-256 hashing utilities and hash chain builder.
 */
@Singleton
class HashUtils @Inject constructor() {

    companion object {
        private const val ALGORITHM = "SHA-256"
        private const val SALT_LENGTH = 16
        private val GENESIS_HASH = ByteArray(32) // all zeros for chain start
    }

    private val secureRandom = SecureRandom()

    /**
     * SHA-256 hash with salt.
     * @return [salt (16)] + [hash (32)]
     */
    fun hashWithSalt(data: ByteArray): ByteArray {
        val salt = ByteArray(SALT_LENGTH).also { secureRandom.nextBytes(it) }
        val digest = MessageDigest.getInstance(ALGORITHM)
        digest.update(salt)
        val hash = digest.digest(data)
        return salt + hash
    }

    /**
     * SHA-256 hash without salt (for chain building).
     */
    fun hash(data: ByteArray): ByteArray {
        return MessageDigest.getInstance(ALGORITHM).digest(data)
    }

    /**
     * Computes hash chain: SHA-256(recordData + previousHash).
     * Uses GENESIS_HASH if no previous hash.
     */
    fun computeChainHash(
        recordData: ByteArray,
        previousHash: ByteArray? = null
    ): ByteArray {
        val prev = previousHash ?: GENESIS_HASH
        val digest = MessageDigest.getInstance(ALGORITHM)
        digest.update(recordData)
        return digest.digest(prev)
    }

    /**
     * Verifies a chain hash link.
     */
    fun verifyChainHash(
        recordData: ByteArray,
        previousHash: ByteArray?,
        expectedHash: ByteArray
    ): Boolean {
        val computed = computeChainHash(recordData, previousHash)
        return computed.contentEquals(expectedHash)
    }

    /**
     * Hex encoding/decoding utilities.
     */
    fun toHex(bytes: ByteArray): String =
        bytes.joinToString("") { "%02x".format(it) }

    fun fromHex(hex: String): ByteArray =
        hex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
}
